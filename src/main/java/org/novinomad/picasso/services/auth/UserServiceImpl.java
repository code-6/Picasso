package org.novinomad.picasso.services.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.utils.PasswordUtil;
import org.novinomad.picasso.commons.utils.ValidationUtil;
import org.novinomad.picasso.domain.erm.entities.auth.Permission;
import org.novinomad.picasso.domain.erm.entities.auth.User;
import org.novinomad.picasso.repositories.jpa.UserRepository;
import org.novinomad.picasso.services.common.EmailService;
import org.slf4j.Logger;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@DependsOn("permissionServiceImpl")

@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final PermissionService permissionService;

    private static final String SUPER_ADMIN_USERNAME = "sa";

    //todo: restore for production
//    @PostConstruct
    void createSuperAdminUser() {
        userRepository.findByUsername(SUPER_ADMIN_USERNAME).ifPresentOrElse((u) -> {}, () -> {
            String randomSuperAdminPassword = PasswordUtil.generateRandomPassword();

            Optional<Permission> rootPermission = permissionService.get(PermissionService.ROOT_PERMISSION_NAME);

            User superAdmin = new User(SUPER_ADMIN_USERNAME, randomSuperAdminPassword)
                    .addPermission(rootPermission.get());

            superAdmin = userRepository.save(superAdmin);

            log.info("super admin user initialized {}", superAdmin);

            emailService.sendSimpleMail("wong.stanislav@gmail.com", "Password for SA picasso", randomSuperAdminPassword);
        });
    }

    @Override
    public User createUser(User user) {

        String userEmail = user.getEmailOptional()
                .orElseThrow(() -> new IllegalArgumentException("user should have an email"));

        if(!ValidationUtil.isValidEmail(userEmail)) {
            throw new IllegalArgumentException(userEmail + " is not a valid email address");
        }
        if(StringUtils.isBlank(user.getUsername())) {
            user.setUsername(userEmail);
        }
        String randomPassword = PasswordUtil.generateRandomPassword();

        user.setPassword(randomPassword);

        user = userRepository.save(user);

        emailService.sendSimpleMail(
                user.getEmailOptional().orElseThrow(()-> new IllegalStateException("user must have an email")),
                "Password for picasso", randomPassword);

        return user;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getById(Collection<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    @Override
    public List<User> get() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public List<User> get(Set<String> usernames) {
        return userRepository.findAllByUsername(usernames);
    }

    
    @Override
    public Optional<User> get(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public User getCurrentUser() {
        throw new NotImplementedException();
    }
}
