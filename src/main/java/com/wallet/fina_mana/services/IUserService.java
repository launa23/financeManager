package com.wallet.fina_mana.services;

import com.wallet.fina_mana.dtos.UserDTO;
import com.wallet.fina_mana.models.User;
import jakarta.servlet.http.HttpServletRequest;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String username, String password) throws Exception;

//    UserResponse getCurrent(HttpServletRequest request) throws Exception;
}
