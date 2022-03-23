package com.bancocaminos.impactbackendapi.user.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.bancocaminos.impactbackendapi.core.exception.authentication.UserAlreadyRegisteredException;
import com.bancocaminos.impactbackendapi.user.rest.model.Role;
import com.bancocaminos.impactbackendapi.user.rest.model.UserModel;
import com.bancocaminos.impactbackendapi.user.usecase.entity.Users;
import com.bancocaminos.impactbackendapi.user.usecase.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { UserService.class, BCryptPasswordEncoder.class })
public class UserServiceTest {

    private static final String SECRET2FA_KEY = "secret2FAKey";
    private static final String USERNAME = "abc@gmail.com";

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void readUserByUsername() {
        Users user = new Users();
        user.setActive(true);
        Optional<Users> userOptional = Optional.of(user);

        when(userRepository.findByUsername(USERNAME)).thenReturn(userOptional);

        Users userResponse = userService.readUserByUsername(USERNAME);

        assertEquals(true, userResponse.isActive());
    }

    @Test
    public void readUserByUsernameByError() {
        when(userRepository.findByUsername(USERNAME)).thenThrow(new EntityNotFoundException());

        Assertions
                .assertThrows(EntityNotFoundException.class,
                        () -> userService.readUserByUsername(USERNAME),
                        "EntityNotFoundException error was expected");
    }

    @Test
    public void findByEmail() {
        Users user = new Users();
        user.setActive(true);
        Optional<Users> userOptional = Optional.of(user);

        when(userRepository.findByUsername(USERNAME)).thenReturn(userOptional);

        Optional<Users> userResponse = userService.findByEmail(USERNAME);

        assertEquals(true, userResponse.isPresent());
        assertEquals(true, userResponse.get().isActive());
    }

    @Test
    public void clearSecretKey() {
        Users user = new Users();
        user.setActive(true);
        user.setSecret2FAKey(SECRET2FA_KEY);

        userService.clearSecretKey(user);

        assertEquals(null, user.getSecret2FAKey());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void updateSecretKey() {
        int number = 666666;
        Users user = new Users();
        user.setActive(true);
        user.setSecret2FAKey(SECRET2FA_KEY);

        userService.updateSecretKey(number, user);

        assertEquals("666666", user.getSecret2FAKey());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void createUserWithError() {
        UserModel usermodel = new UserModel();
        usermodel.setEmail(USERNAME);
        Users user = new Users();
        user.setUsername(USERNAME);
        Optional<Users> userOptional = Optional.of(user);

        when(userRepository.findByUsername(USERNAME)).thenReturn(userOptional);

        Assertions
                .assertThrows(UserAlreadyRegisteredException.class,
                        () -> userService.createUser(usermodel),
                        "UserAlreadyRegisteredException error was expected");
        verify(userRepository).findByUsername(USERNAME);
    }

    @Test
    public void createUser() {
        UserModel usermodel = new UserModel();
        usermodel.setEmail(USERNAME);
        usermodel.setRole(Role.ADMIN);
        usermodel.setPassword("1234");
        usermodel.setPrefix("+34");
        usermodel.setMobileNumber("686343567");
        Users userSaved = new Users();
        userSaved.setActive(true);
        userSaved.setMobileNumber(usermodel.getMobileNumber());
        userSaved.setPrefix(usermodel.getPrefix());
        userSaved.setRole(usermodel.getRole().name());
        userSaved.setUsername(usermodel.getEmail());
        userSaved.setPassword(usermodel.getPassword());

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(userSaved);

        Users user = userService.createUser(usermodel);

        verify(userRepository).findByUsername(USERNAME);
        verify(userRepository).save(any());
        assertEquals(user.getMobileNumber(), usermodel.getMobileNumber());
        assertEquals(user.getRole(), usermodel.getRole().name());
        assertEquals(user.getPrefix(), usermodel.getPrefix());
        assertEquals(user.getUsername(), usermodel.getEmail());
        assertEquals(user.getPassword(), usermodel.getPassword());
    }

}
