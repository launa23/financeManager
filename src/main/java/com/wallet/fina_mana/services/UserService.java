package com.wallet.fina_mana.services;

import com.wallet.fina_mana.Exceptions.DataNotFoundException;
import com.wallet.fina_mana.components.JwtTokenUtil;
import com.wallet.fina_mana.dtos.UserDTO;
import com.wallet.fina_mana.models.User;
import com.wallet.fina_mana.repositories.UserRepository;
import com.wallet.fina_mana.responses.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String username = userDTO.getUsername();
        if (userRepository.existsByUsername(username)){
            throw new DataIntegrityViolationException("Username already exists!");
        }

        User user = User.builder()
                .fullName(userDTO.getFullName())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .dateOfBirth(userDTO.getDateOfBirth())
                .active(true)
                .build();
        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public String login(String username, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid phone number or password");
        }
        User existingUser = optionalUser.get();
        if (!passwordEncoder.matches(password, existingUser.getPassword())){
            throw new BadCredentialsException("Wrong username or password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public User getCurrent(HttpServletRequest request) throws Exception {
        String authenHeader = request.getHeader("Authorization");
        final String token = authenHeader.substring(7);         // cắt bỏ chữ "Bearer " trong chuỗi bearer token để lấy token
        final String username = jwtTokenUtil.extractUsername(token);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user"));

    }
}
