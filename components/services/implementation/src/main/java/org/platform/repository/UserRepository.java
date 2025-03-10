package org.platform.repository;

import org.platform.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity getByEmail(String email);
    UserEntity getByEmailAndUserIdNot(String email, UUID id);
    List<UserEntity> findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(String name, String surname);
}
