package org.example.findmateapi.Component;

import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Security.Jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserComponent {

    @Autowired
    private static UserRepository userRepository;
    @Autowired
    private static JwtUtils jwtUtils;

    public static User getUserFromToken(String token){
        String username = jwtUtils.extractUsername(token);
        if (username == null || username.isEmpty()) {
            return null;
        }
        return userRepository.findByUsername(username).orElse(null);
    }

}
