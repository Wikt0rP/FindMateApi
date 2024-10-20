package org.example.findmateapi.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Request.CreateCs2ProfileRequest;
import org.example.findmateapi.Service.Cs2ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:63342")
public class ProfileController {
    private Cs2ProfileService cs2ProfileService;
    public ResponseEntity<?> createCs2Profile(@RequestBody CreateCs2ProfileRequest createCs2ProfileRequest, HttpServletRequest request){
        return cs2ProfileService.createCs2Profile(createCs2ProfileRequest, request);
    }
}
