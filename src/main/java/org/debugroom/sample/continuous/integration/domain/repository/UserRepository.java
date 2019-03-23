package org.debugroom.sample.continuous.integration.domain.repository;

import org.debugroom.sample.continuous.integration.domain.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
