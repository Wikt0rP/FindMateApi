package org.example.findmateapi.Component;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Entity.UserProfiles;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Response.UserValidationResponse;
import org.example.findmateapi.Security.Jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserValidationResponse getUserFromRequest(HttpServletRequest request){
        String token = JwtUtils.getJwtFromRequest(request);

        if(token == null || !jwtUtils.validateToken(token)){
            return new UserValidationResponse("Can not validate user", null);
        }

        User user = getUserFromToken(token);
        if(user == null){
            return new UserValidationResponse("Cannot find user", null);
        }

        if(!user.isActive()){
            return new UserValidationResponse("User is not active", user);
        }

        return new UserValidationResponse("OK", user);



    }

    public void createUserProfiles(User user){
        UserProfiles userProfiles = new UserProfiles();
        userProfiles.setUser(user);
        user.setUserProfiles(userProfiles);
        userRepository.save(user);
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
