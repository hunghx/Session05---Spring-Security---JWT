package api.example.com.springsecurityjwt.service;

import api.example.com.springsecurityjwt.model.dto.request.FormLogin;
import api.example.com.springsecurityjwt.model.dto.request.FormRegister;
import api.example.com.springsecurityjwt.model.dto.response.JWTResponse;
import api.example.com.springsecurityjwt.model.entity.Role;
import api.example.com.springsecurityjwt.model.entity.RoleName;
import api.example.com.springsecurityjwt.model.entity.User;
import api.example.com.springsecurityjwt.repository.IRoleRepository;
import api.example.com.springsecurityjwt.repository.IUserRepository;
import api.example.com.springsecurityjwt.security.jwt.JWTProvider;
import api.example.com.springsecurityjwt.security.principle.UserDetailsCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private IUserRepository userRepository;
    @Override
    public boolean register(FormRegister formRegister) {
        User user = User.builder()
                .email(formRegister.getEmail())
                .fullName(formRegister.getFullName())
                .username(formRegister.getUsername())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .status(true)
                .build();
        if (formRegister.getRoles()!=null && !formRegister.getRoles().isEmpty()){
            Set<Role> roles = new HashSet<>();
            formRegister.getRoles().forEach(
                    r->{
                        switch (r){
                            case "ADMIN":
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new NoSuchElementException("role not found")));
                            case "MANAGER":
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_MANAGER).orElseThrow(() -> new NoSuchElementException("role not found")));
                            case "USER":
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
                            default:
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
                        }
                    }
            );
            user.setRoleSet(roles);
        }else {
            // mac dinh la user
            Set<Role> roles = new HashSet<>();
            roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
            user.setRoleSet(roles);
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public JWTResponse login(FormLogin formLogin) {
        // xac thực username vaf password
        Authentication authentication = null;
        try {
           authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getUsername(),formLogin.getPassword()));
        }catch (AuthenticationException e){
            throw new RuntimeException("username or password incorrect");
        }
        UserDetailsCustom detailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        String accessToken = jwtProvider.generateAccessToken(detailsCustom);
        return JWTResponse.builder()
                .email(detailsCustom.getEmail())
                .fullName(detailsCustom.getFullName())
                .roleSet(detailsCustom.getAuthorities())
                .status(detailsCustom.isStatus())
                .accessToken(accessToken)
                .build();
    }
}
