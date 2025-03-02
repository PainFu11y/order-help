package org.platform.springJpa;

import org.platform.entity.AccountEntity;
import org.platform.entity.UserEntity;
import org.platform.exceptions.userexceptions.*;
import org.platform.model.User;
import org.platform.model.UserPassword;
import org.platform.repository.AccountRepository;
import org.platform.repository.UserRepository;
import org.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserSpringJpa implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;


    @Override
    public User getUserById(UUID userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with given ID");
        }
        return removeHiddenFields(user.get().toUser());
    }


    @Override
    public User createUser(User user) {
        UserEntity userEntity;
        Optional<AccountEntity> accountEntity;
        if (user.getUserId() != null) {
            throw new UserBadRequestException("User ID must be null");
        }if(user.getPassword() != null){
            throw new UserBadRequestException("Password must be null");
        }

        try {
            userEntity = userRepository.getByEmail(user.getEmail());
        } catch (Exception e) {
            throw new UserApiException("Problem while creating user");
        }
        if (userEntity != null) {
            throw new UserAlreadyExistException("User already exists with given email");
        }

        try {
            accountEntity = accountRepository.findById(user.getAccountId());
        } catch (Exception e) {
            throw new UserApiException("Problem while creating user");
        }
        if (accountEntity.isEmpty()) {
            throw new UserNotFoundException("Account not found with given account id");
        }

        try {
            UserEntity newUserEntity = new UserEntity(user);
            newUserEntity.setAccountEntity(accountEntity.get());

            userEntity = userRepository.save(newUserEntity);
        } catch (Exception e) {
            throw new UserApiException("Problem while creating user: " + e.getMessage());
        }

        return userEntity.toUser();
    }

    @Override
    public void updatePassword(UUID userId, UserPassword password) {
        UserEntity userEntity;
        if(password.getOldPassword() != null && password.getOldPassword().equals(password.getNewPassword())) {
            throw new UserBadRequestException("Old and new password are the same");
        }
        try{
             userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }catch(UserNotFoundException e){
            throw new UserNotFoundException("User not found with given ID");
        }catch(Exception e){
            throw new UserApiException("Problem while updating password");
        }

        if (userEntity.getPassword() == null) {
            throw new UserBadRequestException("User password is null");
        }

        if (!passwordEncoder.matches(password.getOldPassword(), userEntity.getPassword())) {
            throw new InvalidOldPasswordException("Old password is incorrect");
        }

        try {
            userEntity.setPassword(passwordEncoder.encode(password.getNewPassword()));
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new UserApiException("Problem while updating password");
        }
    }



    public List<User> getUsers() {
        List<UserEntity> userEntities;
        try {
            userEntities = userRepository.findAll();
        } catch (Exception e) {
            throw new UserApiException("Problem while getting users");
        }
        return userEntities
                .stream()
                .map(UserEntity::toUser)
                .map(this::removeHiddenFields)
                .toList();
    }

    @Override
    public List<User> getUserByEmail(String email) {
        try {
            UserEntity userEntity = userRepository.getByEmail(email);
            return List.of(removeHiddenFields(userEntity.toUser()));
        } catch (Exception e) {
            throw new UserApiException("Problem during getting users");
        }
    }

    @Override
    public User updateUser(UUID userId, User user) {
        Optional<UserEntity> userEntities;
        UserEntity existingUser;
        if (user.getUserId() != null && !user.getUserId().equals(userId)) {
            throw new UserBadRequestException("ID of body doesn't match with url parameter");
        }if(user.getPassword() != null){
            throw new UserBadRequestException("Password must be null");
        }

        try {
            userEntities = userRepository.findById(userId);
            existingUser = userRepository.getByEmailAndUserIdNot(user.getEmail(), userId);
        } catch (Exception e) {
            throw new UserApiException("Problem during updating user");
        }
        if (userEntities.isEmpty()) {
            throw new UserNotFoundException("User not found with given ID");
        }
        if (existingUser != null) {
            throw new UserAlreadyExistException("User already exists with given email.");
        }
        AccountEntity account = getAccount(user.getAccountId());
        User updatedUser;
        UserEntity userEntity = new UserEntity(user);
        userEntity.setUserId(userId);
        userEntity.setAccountEntity(account);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        try {
            updatedUser = userRepository.save(userEntity).toUser();
        } catch (Exception e) {
            throw new UserApiException("Problem during updating user");
        }
        return updatedUser;
    }

    @Override
    public boolean updateCredential(String email, String password) {
        UserEntity userEntity;
        try {
            userEntity = userRepository.getByEmail(email);
            if(userEntity == null){
                throw new UserNotFoundException("User not found with given email");
            }
        }catch (Exception e){
            throw new UserApiException("Problem during updating user");
        }
        userEntity.setPassword(passwordEncoder.encode(password));
        try{
            userRepository.save(userEntity).toUser();
        }catch (Exception e){
            throw new UserApiException("Problem during updating user credentials");
        }
        return true;
    }

    @Override
    public List<User> search(String name, String surname) {
        return userRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(name,surname)
                .stream().map(UserEntity::toUser).toList();
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with given ID"));
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw new UserApiException("Problem during deleting user");
        }
    }

    private AccountEntity getAccount(UUID accountId) {
        Optional<AccountEntity> accountEntity;
        try {
            accountEntity = accountRepository.findById(accountId);
        } catch (Exception e) {
            throw new UserApiException("Problem during creating user");
        }
        if (accountEntity.isEmpty()) {
            throw new UserNotFoundException("Account not found with given account ID");
        }
        return accountEntity.get();
    }

    private User removeHiddenFields(User user){
        user.setPassword(null);
        return user;
    }
}




