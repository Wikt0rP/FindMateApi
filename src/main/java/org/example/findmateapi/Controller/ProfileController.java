package org.example.findmateapi.Controller;


import org.example.findmateapi.Service.Cs2ProfileService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:63342")
public class ProfileController {
    private Cs2ProfileService cs2ProfileService;

}
