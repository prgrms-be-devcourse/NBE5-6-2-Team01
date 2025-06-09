package com.grepp.synapse4.infra.config;

import com.grepp.synapse4.app.model.restaurant.dto.create.CsvRestaurantDto;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
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

@Configuration
@RequiredArgsConstructor
public class FilReaderJobConfig {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job fileReadWriteJob(JobRepository jobRepository,                    // job-step 일련의 과정 히스토리를 기록하는 db 저장소
                                PlatformTransactionManager transactionManager)  // step 과정이 하나의 transaction 으로 처리되게끔 도와주는 객체
            throws Exception {

                System.out.println("✅✅✅✅✅✅ 빈 등록!!!");

        return new JobBuilder("fileReadWriteJob", jobRepository)
                .incrementer(new RunIdIncrementer())    // job 실행 unique id
                .start(basicStep(jobRepository, transactionManager))
//                .next(detailStep(jobRepository, transactionManager))
//                .next(menuStep(jobRepository, transactionManager))
                .build();
    }

    // Step 1 : 식당 기본 정보 데이터 저장
    // ItemReader - ItemProcessor - ItemWriter
    @JobScope
    @Bean
    public Step basicStep(JobRepository jobRepository, PlatformTransactionManager transactionManager)
            throws Exception {

        System.out.println("🥰🥰🥰🥰🥰🥰 basicStep 1 시작");

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

        System.out.println("☘️☘️☘️☘️☘️☘️ step1 - reader");

        return new FlatFileItemReaderBuilder<CsvRestaurantDto>()
                .name("basicFileItemReader")
                .resource(new FileSystemResource("src/main/resources/restaurantdata/basicInfo_30.csv"))
                .encoding("UTF-8")
                .linesToSkip(1)
                .lineTokenizer(new DelimitedLineTokenizer(){{           // 기본 구분자(,) : 구분자를 기준으로 FieldSet을 만들어줌
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

        System.out.println("🔹🔹🔹🔹🔹 step1 - processr");


        return dto -> Restaurant.builder()
                .publicId(dto.getPublicId())
                .name(dto.getName())
                .branch(dto.getBranch())
                .roadAddress(dto.getAddress())
                .jibunAddress(dto.getJibunAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .category(dto.getCategory())
                .build();
    }

    // Step 1 - ItemWriter
    // Entity DB에 저장!
    @StepScope
    @Bean
    public JpaItemWriter<Restaurant> basicFlatFileItemWriter(EntityManagerFactory entityManagerFactory) {

        System.out.println("라이터 생성 중?!!!");

        JpaItemWriter<Restaurant> writer = new JpaItemWriter<>();

        System.out.println("저장 시도???");

        writer.setEntityManagerFactory(entityManagerFactory);

        System.out.println("?????? 저장 완료?");

        return writer;
    }





}
