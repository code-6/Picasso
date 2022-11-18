package org.novinomad.picasso.services.impl;

import com.github.javafaker.Faker;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.novinomad.picasso.commons.utils.PasswordUtil;
import org.novinomad.picasso.erm.entities.system.Permission;
import org.novinomad.picasso.erm.entities.system.User;
import org.novinomad.picasso.repositories.jpa.UserRepository;
import org.novinomad.picasso.services.IEmailService;
import org.novinomad.picasso.services.IPermissionService;
import org.novinomad.picasso.services.IUserService;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@DependsOn("permissionService")
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final IEmailService emailService;

    private final IPermissionService permissionService;

    @PostConstruct
    void createSuperAdminUser() {

        String randomSuperAdminPassword = PasswordUtil.generateRandomPassword();

        emailService.sendSimpleMail("wong.stanislav@gmail.com", "Password for SA picasso", randomSuperAdminPassword);

        User superAdmin = new User();
        superAdmin.setUserName("sa");
        superAdmin.setPassword(randomSuperAdminPassword);

        Permission fullAccess = permissionService.get("full access")
                .orElseThrow(() -> new BeanCreationException("required permission for super admin is not exists yet"));

        superAdmin.getPermissions().add(fullAccess);

        userRepository.save(superAdmin);
    }

    @Override
    public User save(User user) {

        String randomPassword = PasswordUtil.generateRandomPassword();

        user.setPassword(randomPassword);

        User savedUser = userRepository.save(user);

        emailService.sendSimpleMail(
                user.getEmail().orElseThrow(()-> new IllegalStateException("user must have an email")),
                "Password for picasso", randomPassword);

        return savedUser;
    }

    @Override
    public void deleteById(String username) {
        userRepository.deleteById(username);
    }

    @Override
    public List<User> get(String... strings) {
        return userRepository.findAllById(Arrays.asList(strings));
    }

    @Override
    public Optional<User> get(String s) {
        return Optional.empty();
    }
}
