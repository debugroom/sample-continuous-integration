package org.debugroom.sample.continuous.integration.domain.service;

import org.debugroom.sample.continuous.integration.apinfra.exception.BusinessException;
import org.debugroom.sample.continuous.integration.domain.model.entity.User;
import org.debugroom.sample.continuous.integration.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SampleServiceImpl implements SampleService{

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @Override
    public User getUser(long id) throws BusinessException{
        if(!userRepository.existsById(id)){
            throw new BusinessException("0001", "User does not exist or invalid credential.");
        }
        return userRepository.getOne(id);
    }

    @Override
    @Transactional
    public User updateUser(User user) throws BusinessException{

        User updateTargetUser = userRepository.getOne(user.getId());

        try{
            if(Objects.nonNull(user.getFirstName())){
                updateTargetUser.setFirstName(user.getFirstName());
            }

            if(Objects.nonNull(user.getFamilyName())){
                updateTargetUser.setFamilyName(user.getFamilyName());
            }

            updateTargetUser.setLastUpdateAt(LocalDateTime.now());

        }catch (NullPointerException | EntityNotFoundException e){
            throw new BusinessException("0001", "User does not exist or invalid credential.");
        }

        return userRepository.save(updateTargetUser);

    }

}
