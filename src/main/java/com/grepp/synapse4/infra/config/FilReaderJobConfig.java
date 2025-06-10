package com.grepp.synapse4.infra.config;

import com.grepp.synapse4.app.model.restaurant.dto.create.CsvRestaurantDetailDto;
import com.grepp.synapse4.app.model.restaurant.dto.create.CsvRestaurantDto;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.entity.RestaurantDetail;
import com.grepp.synapse4.app.model.restaurant.repository.RestaurantDetailRepository;
import com.grepp.synapse4.app.model.restaurant.repository.RestaurantRepository;
import com.grepp.synapse4.infra.batch.RestaurantDetailProcessor;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ObjectUtils;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FilReaderJobConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final RestaurantDetailProcessor restaurantDetailProcessor;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantDetailRepository restaurantDetailRepository;

    @Bean
    public Job fileReadWriteJob(JobRepository jobRepository,                    // job-step 일련의 과정 히스토리를 기록하는 db 저장소
                                PlatformTransactionManager transactionManager)  // step 과정이 하나의 transaction 으로 처리되게끔 도와주는 객체
            throws Exception {

        return new JobBuilder("fileReadWriteJob", jobRepository)
                .incrementer(new RunIdIncrementer())    // job 실행 unique id
                .start(basicStep(jobRepository, transactionManager))
                .next(detailStep(jobRepository, transactionManager))
//                .next(menuStep(jobRepository, transactionManager))
                .build();
    }

    // Step 1 : 식당 기본 정보 데이터 저장
    // ItemReader - ItemProcessor - ItemWriter
    @JobScope
    @Bean
    public Step basicStep(JobRepository jobRepository, PlatformTransactionManager transactionManager)
            throws Exception {

        return new StepBuilder("basicStep", jobRepository)
                .<CsvRestaurantDto, Restaurant>chunk(10, transactionManager)
                .reader(basicFlatFileItemReader())
                .processor(basicFlatFileItemProcessor())
                .writer(basicFlatFileItemWriter(entityManagerFactory))
                .build();
    }

    // Step 1 - ItemReader
    @StepScope
    @Bean
    public FlatFileItemReader<CsvRestaurantDto> basicFlatFileItemReader() throws Exception {

        return new FlatFileItemReaderBuilder<CsvRestaurantDto>()
                .name("basicFileItemReader")
                .resource(new FileSystemResource("src/main/resources/restaurantdata/basicInfo_all.csv"))
                .encoding("UTF-8")
                .linesToSkip(1)
                .lineTokenizer(new DelimitedLineTokenizer(){{
                    // 기본 구분자(,) : 구분자를 기준으로 FieldSet을 만들어줌
                    setNames("publicId","name","branch","address","jibunAddress","latitude","longitude","tel","category","businessName","description");
                }})
//                .fieldSetMapper(new FieldSetMapper())     //csv 파일 헤더와 dto가 일치하지 않을 때 수동 매핑하기 위한 인터페이스
                .fieldSetMapper(new BeanWrapperFieldSetMapper<CsvRestaurantDto>() {{        // 자동 매핑 : csv 헤더와 Dto의 필드가 완벽히 일치할 때만 사용
                    setTargetType(CsvRestaurantDto.class);
                }})
                .build();
    }

    // Step 1 - ItemProcessor
    // Dto -> Entity
    @StepScope
    @Bean
    public ItemProcessor<CsvRestaurantDto, Restaurant> basicFlatFileItemProcessor() {

        return dto -> {

            // 데이터 유효성 검증
            // 위경도 값 없을 시 skip
            if (ObjectUtils.isEmpty(dto.getLatitude())
                    || ObjectUtils.isEmpty(dto.getLongitude())) {
                log.debug("위경도 값 부재 {}", dto.getName());
                return null;
            }
            // 이미 존재하는 publicId skip
            if(restaurantRepository.existsByPublicId(dto.getPublicId())){
                log.info("이미 존재하는 publicId={}", dto.getPublicId());
                return null;
            }

            // restaurant 생성
            Restaurant restaurant = Restaurant.builder()
                    .publicId(dto.getPublicId())
                    .name(dto.getName())
                    .branch(dto.getBranch())
                    .roadAddress(dto.getAddress())
                    .jibunAddress(dto.getJibunAddress())
                    .latitude(dto.getLatitude())
                    .longitude(dto.getLongitude())
                    .category(dto.getCategory())
                    .build();

            // restaurant 저장
//            Restaurant savedRes = restaurantRepository.save(restaurant);

            // detail 생성
            // tel이 csv에는 basic에, 우리 DB에는 detail에 있어야 하기 때문
            // 아직 id 값은 없으므로 builder만 생성
            RestaurantDetail detail = RestaurantDetail.builder()
                    .restaurant(restaurant) // 연관관계
                    .tel(dto.getTel())
                    .build();

            restaurant.setDetail(detail);

            // restaurant와 함께 저장되도록 (아직 res의 id가 없기 때문에 Res 객체 자체로 찾아가야 함)
//            restaurantDetailRepository.save(detail);

            return restaurant;
        };
    }

    // Step 1 - ItemWriter
    // Entity DB에 저장!
    @StepScope
    @Bean
    public JpaItemWriter<Restaurant> basicFlatFileItemWriter(EntityManagerFactory entityManagerFactory) {

        JpaItemWriter<Restaurant> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }


    // Step 2 : 식당 상세 정보 데이터 저장
    @JobScope
    @Bean
    public Step detailStep(JobRepository jobRepository, PlatformTransactionManager transactionManager)
            throws Exception {

        System.out.println("🥰🥰🥰🥰🥰🥰 basicStep 2 시작");

        return new StepBuilder("detailStep", jobRepository)
                .<CsvRestaurantDetailDto, RestaurantDetail>chunk(10, transactionManager)
                .reader(detailFlatFileItemReader())
                .processor(detailFlatFileItemProcessor(restaurantDetailProcessor))
                .writer(detailFlatFileItemWriter(entityManagerFactory))
                .build();
    }

    // Step 2 - ItemReader
    @StepScope
    @Bean
    public FlatFileItemReader<CsvRestaurantDetailDto> detailFlatFileItemReader() throws Exception {

        System.out.println("☘️☘️☘️☘️☘️☘️ step1 - reader");

        return new FlatFileItemReaderBuilder<CsvRestaurantDetailDto>()
                .name("detailFileItemReader")
                .resource(new FileSystemResource("src/main/resources/restaurantdata/detailInfo_all.csv"))
                .encoding("UTF-8")
                .linesToSkip(1)
                .lineTokenizer(new DelimitedLineTokenizer(){{
                    // 기본 구분자(,) : 구분자를 기준으로 FieldSet을 만들어줌
                    setNames("publicId","name","branch","regionName","parkingRaw","wifiRaw","playroom","foreignMenu","toiletInfo","dayOff","rowBusinessTime","deliveryRow","onlineReservationInfo","homePageURL","landmarkName","landmarkLatitude","landmarkLongitude","distanceFromLandmark","smartOrder","signatureMenus","status","hashtags","areaInfo"

                    );
                }})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<CsvRestaurantDetailDto>() {{        // 자동 매핑 : csv 헤더와 Dto의 필드가 완벽히 일치할 때만 사용
                    setTargetType(CsvRestaurantDetailDto.class);
                }})
                .build();
    }

    // Step 2 - ItemProcessor
    // Dto -> Entity
    // 같은 publicId를 가진 기본 식당 정보 데이터를 찾아 매핑해야 하므로, 별도 클래스로 분리
    @Bean
    @StepScope
    public ItemProcessor<CsvRestaurantDetailDto, RestaurantDetail> detailFlatFileItemProcessor(
            RestaurantDetailProcessor processor
    ) {
        System.out.println("프로세스 실행");
        return processor;
    }


    // Step 2 - ItemWriter
    // Entity DB에 저장!
    @StepScope
    @Bean
    public JpaItemWriter<RestaurantDetail> detailFlatFileItemWriter(EntityManagerFactory entityManagerFactory) {

        System.out.println("라이터 실행");

        JpaItemWriter<RestaurantDetail> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }





}
