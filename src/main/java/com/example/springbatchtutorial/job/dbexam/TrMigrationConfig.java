package com.example.springbatchtutorial.job.dbexam;

import com.example.springbatchtutorial.job.core.domain.account.Account;
import com.example.springbatchtutorial.job.core.domain.account.AccountRepository;
import com.example.springbatchtutorial.job.core.domain.order.Order;
import com.example.springbatchtutorial.job.core.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;

/**
 * docs : 주문테이블 -> 정산테이블 데이터 이관
 * run : --spring.batch.job.names=trMigrationJob
 */

@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;

    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return jobBuilderFactory.get("trMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(trMigrationStep)
                .build();
    }

    @Bean
    @JobScope
    public Step trMigrationStep(RepositoryItemReader<Order> itemReader,
                                ItemProcessor<Order, Account> trOrderProcessor,
                                ItemWriter<Account> itemWriter) {
        return stepBuilderFactory.get("trMigrationStep")
                .<Order, Account>chunk(5)
                .reader(itemReader)
                .processor(trOrderProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Order> trOrderReader() {
        return new RepositoryItemReaderBuilder<Order>()
                .name("trOrderReader")
                .repository(orderRepository)
                .methodName("findAll")
                .pageSize(5)
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<Account> trOrderWriter() {
        return new RepositoryItemWriterBuilder<Account>()
                .repository(accountRepository)
                .methodName("save")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Order, Account> trOrderProcessor() {
        return Account::new;
    }

// ItemWriter 사용
//    @Bean
//    @StepScope
//    public ItemWriter<Account> trOrderWriter() {
//        return items -> items.forEach(item -> accountRepository.save(item));
//    }
}
