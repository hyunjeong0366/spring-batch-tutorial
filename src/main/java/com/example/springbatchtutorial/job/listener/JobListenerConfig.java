package com.example.springbatchtutorial.job.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobListenerConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobListnerJob() {
        return jobBuilderFactory.get("jobListnerJob")
                .incrementer(new RunIdIncrementer())        // job seq 순서대로 부여
                .listener(new JobLoggerListener())
                .start(jobListnerStep())
                .build();

    }

    @Bean
    @JobScope
    public Step jobListnerStep() {
        return stepBuilderFactory.get("jobListnerStep")
                .tasklet(jobListerTasklet())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet jobListerTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("This is jobListerTasklet");
            return RepeatStatus.FINISHED;
//            throw new Exception("Fail !! ");
        };
    }
}
