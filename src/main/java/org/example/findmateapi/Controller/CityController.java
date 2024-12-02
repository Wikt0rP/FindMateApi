package org.example.findmateapi.Controller;

import org.example.findmateapi.Entity.City;
import org.example.findmateapi.Service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/city")
@CrossOrigin(origins = "http://localhost:63342")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/all")
    public List<City> getAllCities(){
        return cityService.getCities();
    }
}
