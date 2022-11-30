package com.example.springbatchtutorial.job.validatedparam;

import com.example.springbatchtutorial.job.validatedparam.validator.FileParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * docs : 파일 이름 파라미터 전달, 검증
 * --spring.batch.job.names=validateParamJob -fileName=test.csv
 */

@Configuration
@RequiredArgsConstructor
public class ValidatedParamJobConfig {
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job validateParamJob(Step validateParamStep) {
        return jobBuilderFactory.get("validateParamJob")
                .incrementer(new RunIdIncrementer())        // job seq 순서대로 부여
//                .validator(new FileParamValidator())
                .validator(multiplueValidator())
                .start(validateParamStep)
                .build();

    }

    // validator 여러 개
    private CompositeJobParametersValidator multiplueValidator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(Arrays.asList(new FileParamValidator()));

        return validator;
    }

    @Bean
    @JobScope
    public Step validateParamStep(Tasklet validatedParamTasklet) {
        return stepBuilderFactory.get("validateParamStep")
                .tasklet(validatedParamTasklet)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet validatedParamTasklet(@Value("#{jobParameters['fileName']}") String fileName) {
        return (contribution, chunkContext) -> {
            System.out.println("fileName = " + fileName);
            System.out.println("validated Param Tasklet");
            return RepeatStatus.FINISHED;
        };
    }
}
