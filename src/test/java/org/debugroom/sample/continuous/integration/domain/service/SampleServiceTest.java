package org.debugroom.sample.continuous.integration.domain.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import org.debugroom.sample.continuous.integration.apinfra.exception.BusinessException;
import org.debugroom.sample.continuous.integration.apinfra.junit.category.IntegrationTest;
import org.debugroom.sample.continuous.integration.apinfra.junit.category.UnitTest;
import org.debugroom.sample.continuous.integration.domain.repository.UserRepository;
import org.debugroom.sample.continuous.integration.config.TestConfig;
import org.debugroom.sample.continuous.integration.domain.model.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(Enclosed.class)
public class SampleServiceTest {

    @Category(UnitTest.class)
    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = TestConfig.class)
    public static class SampleServiceUnitTest{

        @Autowired
        SampleService sampleService;

        @MockBean
        UserRepository userRepository;

        @Before
        public void setUp(){
            User beforeUpdateUser = User.builder()
                    .id(new Long(1))
                    .firstName("(・∀・)")
                    .build();
            User otherUser = User.builder()
                    .id(new Long(2))
                    .firstName("(・ω・`)")
                    .build();
            User normalUpdateUser = User.builder()
                    .id(new Long(1))
                    .firstName("UPDATE_NORMAL_TEST")
                    .build();
            List<User> findAllUsers = new ArrayList<>();
            findAllUsers.add(beforeUpdateUser);
            findAllUsers.add(otherUser);
            Mockito.when(userRepository.getOne(new Long(1))).thenReturn(beforeUpdateUser);
            Mockito.when(userRepository.getOne(new Long(2))).thenReturn(otherUser);
            Mockito.when(userRepository.existsById(new Long(2))).thenReturn(true);
            Mockito.when(userRepository.existsById(new Long(3))).thenReturn(false);
            Mockito.when(userRepository.save(normalUpdateUser)).thenReturn(normalUpdateUser);
            Mockito.when(userRepository.findAll()).thenReturn(findAllUsers);
        }

        @Test
        public void getUserNormalTest() throws BusinessException{
            User user = sampleService.getUser(new Long(2));
            assertThat(user.getFirstName(), is("(・ω・`)"));
        }

        @Test(expected = BusinessException.class)
        public void getUserAbnormalTest() throws BusinessException{
            sampleService.getUser(new Long(4));
        }

        @Test
        public void updateUserNormalTest() throws BusinessException{
            User updateUser = User.builder()
                    .id(new Long(1))
                    .firstName("UPDATE_NORMAL_TEST")
                    .build();

            sampleService.updateUser(updateUser);
            List<User> users = sampleService.getUsers();
            users.forEach(user -> {
                log.info(user.getFirstName());
                switch (user.getId().toString()) {
                    case "1": assertThat(user.getFirstName() , is("UPDATE_NORMAL_TEST")); break;
                    case "2": assertThat(user.getFirstName() , is("(・ω・`)")); break;
                }
            });
        }

        @Test(expected = BusinessException.class)
        public void updateUserAbnormalTest() throws BusinessException{
            User updateUser = User.builder()
                    .id(new Long(0))
                    .build();
            sampleService.updateUser(updateUser);
        }

    }

    @Category(IntegrationTest.class)
    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = TestConfig.class)
    public static class SampleServiceIntegrationTest{
        @Autowired
        SampleService sampleService;

        @Test
        public void getUsersTest(){
            List<User> users = sampleService.getUsers();
            users.forEach(user -> {
                log.info(user.getFirstName());
                switch (user.getId().toString()) {
                    case "1": assertThat(user.getFirstName() , is("Kohei")); break;
                    case "2": assertThat(user.getFirstName() , is("(・∀・)")); break;
                    case "3": assertThat(user.getFirstName() , is("(・ω・`)")); break;
                }
            });
        }

        @Test
        public void updateUserTest() throws BusinessException {
            User updateUser = User.builder()
                .id(new Long(1)).firstName("UPDATE")
                .build();
            sampleService.updateUser(updateUser);
            List<User> users = sampleService.getUsers();
            users.forEach(user -> {
                log.info(user.getFirstName());
                switch (user.getId().toString()) {
                    case "1": assertThat(user.getFirstName() , is("UPDATE")); break;
                    case "2": assertThat(user.getFirstName() , is("(・∀・)")); break;
                    case "3": assertThat(user.getFirstName() , is("(・ω・`)")); break;
                }
            });
        }

    }


}
