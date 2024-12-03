package org.example.findmateapi.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Entity.City;
import org.example.findmateapi.Request.GetCitiesInRadiusRequest;
import org.example.findmateapi.Service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/city")
@CrossOrigin(origins = "http://localhost:63342")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/test")
    public ResponseEntity<?> getCityByName(@RequestBody String cityName){
        City city = cityService.getCityByName(cityName);
        if(city == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(city);
    }

    @PostMapping("/citiesInRadius")
    public ResponseEntity<?> getCitiesInRadius(@RequestBody GetCitiesInRadiusRequest getCitiesInRadiusRequest, HttpServletRequest request){
        return cityService.getCitiesInRadius(getCitiesInRadiusRequest, request);
    }
}
