package org.debugroom.sample.continuous.integration.app.model;

import java.util.List;
import java.util.stream.Collectors;

import org.debugroom.sample.continuous.integration.domain.model.entity.User;

public interface UserResourceMapper {

    public static UserResource map(User user){
        return UserResource.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .familyName(user.getFamilyName())
                .build();
    }

    public static User mapToEntity(UserResource userResource){
        return User.builder()
                .id(userResource.getId())
                .firstName(userResource.getFirstName())
                .familyName(userResource.getFamilyName())
                .build();
    };

    public static List<UserResource> map(List<User> users){
        return users.stream().map(
                user ->  UserResource.builder()
                             .id(user.getId())
                             .firstName(user.getFirstName())
                             .familyName(user.getFamilyName())
                             .build())
                .collect(Collectors.toList());
    }

}
