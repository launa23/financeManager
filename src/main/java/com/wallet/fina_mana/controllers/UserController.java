package com.wallet.fina_mana.controllers;

import com.wallet.fina_mana.dtos.UserDTO;
import com.wallet.fina_mana.dtos.UserLoginDTO;
import com.wallet.fina_mana.dtos.WalletDTO;
import com.wallet.fina_mana.models.User;
import com.wallet.fina_mana.responses.LoginResponse;
import com.wallet.fina_mana.responses.UserResponse;
import com.wallet.fina_mana.services.UserService;
import com.wallet.fina_mana.services.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user")
public class UserController {
    private final UserService userService;
    private final WalletService walletService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult  result){
        try {
            if (result.hasErrors()) {
                List<String> errorMess = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMess);
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Password does not match!");
            }
            WalletDTO walletDTO = new WalletDTO("Ví tiền mặt", "0", "wallet");
            User user = userService.createUser(userDTO);
            walletService.createWallet(walletDTO, user, false);
            return ResponseEntity.ok("Register successfully!");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        try {
            String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
            Map<String, String> result = new HashMap<>();
            result.put("token", token);
            LoginResponse loginResponse = new LoginResponse("Successfully", token, "");
            return ResponseEntity.ok(loginResponse);
        }
        catch (Exception e){
            LoginResponse loginResponse = new LoginResponse("Failure", "", e.getMessage());
            return ResponseEntity.ok(loginResponse);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request){
        try{

            User user = userService.getCurrent(request);
            UserResponse userResponse = UserResponse.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .dateOfBirth(user.getDateOfBirth())
                    .build();
            return ResponseEntity.ok(userResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
