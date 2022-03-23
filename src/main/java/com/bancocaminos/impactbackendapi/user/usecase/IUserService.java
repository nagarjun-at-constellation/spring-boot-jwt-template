package com.bancocaminos.impactbackendapi.user.usecase;

import java.util.Optional;

import com.bancocaminos.impactbackendapi.user.rest.model.UserModel;
import com.bancocaminos.impactbackendapi.user.usecase.entity.Users;

public interface IUserService {
    public Users readUserByUsername(String username);

    public Users createUser(UserModel userModel);

    public Optional<Users> findByEmail(String email);

    public void clearSecretKey(Users user);

    public void updateSecretKey(int number, Users user);
}
