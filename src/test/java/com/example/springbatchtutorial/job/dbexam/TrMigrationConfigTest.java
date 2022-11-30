package com.example.springbatchtutorial.job.dbexam;

import com.example.springbatchtutorial.SpringBatchTestConfig;
import com.example.springbatchtutorial.job.core.domain.account.AccountRepository;
import com.example.springbatchtutorial.job.core.domain.order.Order;
import com.example.springbatchtutorial.job.core.domain.order.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, TrMigrationConfig.class})
class TrMigrationConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void cleanUpEach() {
        orderRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void success_noData() throws Exception {
        //when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(0, accountRepository.count());
    }

    @Test
    public void success_existData() throws Exception {
        //given
        Order order1 = Order.builder().orderItem("kakao gift")
                .price(15000)
                .orderDate(LocalDateTime.now())
                .build();

        Order order2 = Order.builder().orderItem("naver gift")
                .price(15000)
                .orderDate(LocalDateTime.now())
                .build();

        orderRepository.save(order1);
        orderRepository.save(order2);

        //when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(2, accountRepository.count());
    }
}