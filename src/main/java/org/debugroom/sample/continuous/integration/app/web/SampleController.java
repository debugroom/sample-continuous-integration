package org.debugroom.sample.continuous.integration.app.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.debugroom.sample.continuous.integration.apinfra.exception.BusinessException;
import org.debugroom.sample.continuous.integration.app.model.UserResource;
import org.debugroom.sample.continuous.integration.app.model.UserResourceMapper;
import org.debugroom.sample.continuous.integration.domain.service.SampleService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
public class SampleController {

    @Autowired
    SampleService sampleService;

    @GetMapping("/users")
    public List<UserResource> getUsers(){
        return UserResourceMapper.map(sampleService.getUsers());
    }

    @GetMapping("/users/{userId}")
    public UserResource getUser(@PathVariable String userId) throws BusinessException{
        return UserResourceMapper.map(sampleService.getUser(new Long(userId)));
    }

    @PutMapping("/users/{userId}")
    public UserResource updateUser(@RequestBody @Valid UserResource userResource) throws BusinessException {
        return UserResourceMapper.map(sampleService.updateUser(UserResourceMapper.mapToEntity(userResource)));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(
            BusinessException businessException){
        return new ResponseEntity<String>(
                businessException.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
