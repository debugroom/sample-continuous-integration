package org.debugroom.sample.continuous.integration.domain.service;

import java.util.List;

import org.debugroom.sample.continuous.integration.apinfra.exception.BusinessException;
import org.debugroom.sample.continuous.integration.domain.model.entity.User;

public interface SampleService {

    public List<User> getUsers();

    public User getUser(long id) throws BusinessException;

    public User updateUser(User user) throws BusinessException;

}
