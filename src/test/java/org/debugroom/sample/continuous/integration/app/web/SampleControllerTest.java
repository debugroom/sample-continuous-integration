package org.debugroom.sample.continuous.integration.app.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import org.debugroom.sample.continuous.integration.apinfra.junit.category.End2EndTest;
import org.debugroom.sample.continuous.integration.apinfra.junit.category.UnitTest;
import org.debugroom.sample.continuous.integration.app.model.UserResource;
import org.debugroom.sample.continuous.integration.config.TestConfig;
import org.debugroom.sample.continuous.integration.domain.model.entity.User;
import org.debugroom.sample.continuous.integration.domain.service.SampleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(Enclosed.class)
public class SampleControllerTest {

    @Category(UnitTest.class)
    @RunWith(SpringRunner.class)
    @WebMvcTest(controllers = SampleController.class)
    public static class SampleControllerUnitTest{

        @Autowired
        ObjectMapper objectMapper;

        @Autowired
        MockMvc mockMvc;

        @MockBean
        SampleService sampleService;

        @Before
        public void setUp() throws Exception {
            User beforeUpdateUser = User.builder()
                    .id(new Long(1))
                    .firstName("(・∀・)")
                    .familyName("Test")
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
            Mockito.when(sampleService.getUser(new Long(1))).thenReturn(beforeUpdateUser);
            Mockito.when(sampleService.getUsers()).thenReturn(findAllUsers);
            Mockito.when(sampleService.updateUser(normalUpdateUser)).thenReturn(normalUpdateUser);
        }

        @Test
        public void getUserNormalTest() throws Exception{

            UserResource userResource = UserResource.builder()
                    .id(1)
                    .firstName("(・∀・)")
                    .familyName("Test")
                    .build();

            mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                    .andExpect(MockMvcResultMatchers.content().string(
                            objectMapper.writeValueAsString(userResource)
                    ));

        }

        @Test
        public void updateUserVerifingInputAbnormalTest() throws Exception{

            UserResource userResource = UserResource.builder()
                    .id(1)
                    .firstName("(・∀・)")
                    .familyName(null)
                    .build();

            mockMvc.perform(MockMvcRequestBuilders
                    .put("/api/v1/users/{userId}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userResource)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

        }

    }

    @Category(End2EndTest.class)
    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = TestConfig.class,
            webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    public static class SampleControllerE2ETest{

        @Autowired
        TestRestTemplate testRestTemplate;

        @LocalServerPort
        int port;

        private String server;

        @Before
        public void setUp(){
            server = "http://localhost:" + port;
        }

        @Test
        public void getUserNormalTest(){

            ResponseEntity<UserResource> responseEntity = testRestTemplate
                    .getForEntity(server + "/api/v1/users/1", UserResource.class);

            UserResource userResource = responseEntity.getBody();

            assertThat(userResource.getId(), is(1L));
            assertThat(userResource.getFirstName(), is("Kohei"));
            assertThat(userResource.getFamilyName(), is("Kawabata"));

        }

        @Test
        public void getUserAbnormalTest(){

            ResponseEntity<String> responseEntity
                    = testRestTemplate.getForEntity(server + "/api/v1/users/0",
                    String.class);

            String errorMessage = responseEntity.getBody();

            assertThat(errorMessage, is("User does not exist or invalid credential."));

        }

        @Test
        public void updateUserTest(){

            ResponseEntity<UserResource[]> responseEntity = testRestTemplate
                    .getForEntity(server + "/api/v1/users", UserResource[].class);

            List<UserResource> userResources = Arrays.asList(responseEntity.getBody());

            userResources.forEach(userResource -> {
                log.info(userResource.getFirstName());
                switch (Long.toString(userResource.getId())) {
                    case "1":
                        assertThat(userResource.getFirstName() , is("Kohei"));
                        assertThat(userResource.getFamilyName() , is("Kawabata"));
                        break;
                    case "2":
                        assertThat(userResource.getFirstName() , is("(・∀・)"));
                        assertThat(userResource.getFamilyName() , is("ふぉふぉふぉ"));
                        break;
                    case "3":
                        assertThat(userResource.getFirstName() , is("(・ω・`)"));
                        assertThat(userResource.getFamilyName() , is("しょぼーん"));
                        break;
                }
            });

            UserResource updateUserResource = UserResource.builder()
                                                .id(1)
                                                .firstName("UPDATE_FIRST_NAME")
                                                .familyName("UPDATE_FAMILY_NAME")
                                                .build();

            ResponseEntity<UserResource> updateResponseEntity
                    = testRestTemplate.exchange(
                    UriComponentsBuilder.fromHttpUrl(server + "/api/v1/users/{userId}").build().expand(updateUserResource.getId()).toUri(),
                    HttpMethod.PUT, new HttpEntity<UserResource>(updateUserResource),
                    UserResource.class);

            responseEntity = testRestTemplate.getForEntity(server + "/api/v1/users",
                    UserResource[].class);

            List<UserResource> updateResultUserResources =
                    Arrays.asList(responseEntity.getBody());

            updateResultUserResources.forEach(userResource -> {
                log.info(userResource.getFirstName());
                switch (Long.toString(userResource.getId())) {
                    case "1":
                        assertThat(userResource.getFirstName() , is("UPDATE_FIRST_NAME"));
                        assertThat(userResource.getFamilyName() , is("UPDATE_FAMILY_NAME"));
                        break;
                    case "2":
                        assertThat(userResource.getFirstName() , is("(・∀・)"));
                        assertThat(userResource.getFamilyName() , is("ふぉふぉふぉ"));
                        break;
                    case "3":
                        assertThat(userResource.getFirstName() , is("(・ω・`)"));
                        assertThat(userResource.getFamilyName() , is("しょぼーん"));
                        break;
                }
            });

        }

        @Test
        public void updateUserAbnormalTest(){

            UserResource updateUserResource = UserResource.builder()
                    .id(0)
                    .firstName("UPDATE_FIRST_NAME")
                    .familyName("UPDATE_FAMILY_NAME")
                    .build();

            ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                    UriComponentsBuilder.fromHttpUrl(server + "/api/v1/users/{userId}").build().expand(updateUserResource.getId()).toUri(),
                    HttpMethod.PUT, new HttpEntity<UserResource>(updateUserResource),
                    String.class);

            String errorMessage = responseEntity.getBody();

            assertThat(errorMessage, is("User does not exist or invalid credential."));

        }

    }

}
