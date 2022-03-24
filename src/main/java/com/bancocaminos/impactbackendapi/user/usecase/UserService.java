package com.bancocaminos.impactbackendapi.user.usecase;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.bancocaminos.impactbackendapi.core.exception.authentication.UserAlreadyRegisteredException;
import com.bancocaminos.impactbackendapi.user.rest.model.UserModel;
import com.bancocaminos.impactbackendapi.user.usecase.entity.Users;
import com.bancocaminos.impactbackendapi.user.usecase.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Users readUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Users createUser(UserModel userModel) {
        Users user = new Users();
        Optional<Users> byUsername = userRepository.findByUsername(userModel.getEmail());
        if (byUsername.isPresent()) {
            throw new UserAlreadyRegisteredException();
        }
        user.setUsername(userModel.getEmail());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setRole(userModel.getRole().name());
        user.setActive(true);
        user.setUsing2FA(true);
        user.setMobileNumber(userModel.getMobileNumber());
        user.setPrefix(userModel.getPrefix());
        return userRepository.save(user);
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByUsername(email);
    }

    @Override
    public void clearSecretKey(Users user) {
        user.setSecret2FAKey(null);
        userRepository.save(user);
    }

    @Override
    public void updateSecretKey(int number, Users user) {
        String secretKey = String.valueOf(number);
        user.setSecret2FAKey(secretKey);
        userRepository.save(user);
    }
}
