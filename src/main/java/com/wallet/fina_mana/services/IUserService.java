package com.wallet.fina_mana.services;

import com.wallet.fina_mana.dtos.UserDTO;
import com.wallet.fina_mana.models.User;
import com.wallet.fina_mana.responses.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String username, String password) throws Exception;

    User getCurrent(HttpServletRequest request) throws Exception;
}
