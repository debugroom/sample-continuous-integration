package org.debugroom.sample.continuous.integration.domain.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.debugroom.sample.continuous.integration.apinfra.junit.category.UnitTest;
import org.debugroom.sample.continuous.integration.domain.model.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Category(UnitTest.class)
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserResourceRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Before
    public void before(){
        testEntityManager.persist(User.builder()
                .id(new Long(1)).firstName("test1").familyName("TEST")
                .ver(0).lastUpdateAt(LocalDateTime.now())
                .build());
        testEntityManager.persist(User.builder()
                .id(new Long(2)).firstName("test2").familyName("TEST")
                .ver(0).lastUpdateAt(LocalDateTime.now())
                .build());
        testEntityManager.persist(User.builder()
                .id(new Long(3)).firstName("test3").familyName("TEST")
                .ver(0).lastUpdateAt(LocalDateTime.now())
                .build());
    }

    @Test
    public void testFindAll() throws Exception{
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            log.info(user.getFirstName());
            switch (user.getId().toString()) {
                case "1": assertThat(user.getFirstName() , is("test1")); break;
                case "2": assertThat(user.getFirstName() , is("test2")); break;
                case "3": assertThat(user.getFirstName() , is("test3")); break;
            }
        });
    }

}
