package org.example.findmateapi.Service;


import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Repository.Cs2ProfileRepository;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Security.Jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;



@Service
public class Cs2ProfileService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Cs2ProfileRepository cs2ProfileRepository;

    private static final Logger logger = LoggerFactory.getLogger(Cs2ProfileService.class);
    @Autowired
    private JwtUtils jwtUtils;

// TODO: Remake this

//    /**
//     * Creates a new Cs2Profile for the user
//     * @param createCs2ProfileRequest CreateCs2ProfileRequest object - all data needed to create a new Cs2Profile
//     * @param request Auth request
//     * @return ResponseEntity with message
//     */
//    public ResponseEntity<String> createCs2Profile(CreateCs2ProfileRequest createCs2ProfileRequest, HttpServletRequest request) {
//        String token = getJwtFromRequest(request);
//        if(token == null || !jwtUtils.validateToken(token)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
//
//        }else{
//            User user = getUserFromToken(token);
//
//            if(user != null){
//                try {
//                    Cs2Profile cs2Profile = new Cs2Profile(user ,createCs2ProfileRequest.getRank());
//                    cs2ProfileRepository.save(cs2Profile);
//                    user.setCs2Profile(cs2Profile);
//                    userRepository.save(user);
//
//                    return ResponseEntity.status(HttpStatus.CREATED).body("Cs2 Profile created successfully");
//
//                } catch (Exception e) {
//                    logger.error("Error creating Cs2 Profile", e);
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Cs2 Profile");
//                }
//            }
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//    }
    private User getUserFromToken(String token){
        String username = jwtUtils.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElse(null);
    }

}
