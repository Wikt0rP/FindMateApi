package org.example.findmateapi.Component;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Security.Jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserComponent {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;





    /**
     * Get user from request
     * @return HashMap<String, User> where you can find values: <br>
     * "Can not validate user"<br>
     * "Cannot find user"<br>
     * "User is not active" + User<br>
     * "OK" + User<br>
     */
    public HashMap<String, User> getUserFromRequest(HttpServletRequest request){
        String token = JwtUtils.getJwtFromRequest(request);
        HashMap<String, User> response = new HashMap<>();
        if(token == null || !jwtUtils.validateToken(token)){
            response.put("Can not validate user", null);
            return response;
        }
        User user = getUserFromToken(token);
        if(user == null){
            response.put("Cannot find user", null);
            return response;
        }

        if(!user.isActive()){
            response.put("User is not active", user);
        }
        else{
            response.put("OK", user);
        }
        return response;

    }

    @Deprecated
    public User getUserFromToken(String token){
        String username = jwtUtils.extractUsername(token);
        if (username == null || username.isEmpty()) {
            return null;
        }
        return userRepository.findByUsername(username).orElse(null);
    }

}
