package api.example.com.springsecurityjwt.service;

import api.example.com.springsecurityjwt.model.dto.request.FormLogin;
import api.example.com.springsecurityjwt.model.dto.request.FormRegister;
import api.example.com.springsecurityjwt.model.dto.response.JWTResponse;

public interface IUserService {
      boolean register(FormRegister formRegister);
        JWTResponse login(FormLogin formLogin);
}
