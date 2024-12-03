package org.example.findmateapi.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.example.findmateapi.Component.UserComponent;
import org.example.findmateapi.Entity.City;
import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Repository.CityRepository;
import org.example.findmateapi.Request.GetCitiesInRadiusRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

@Service
public class CityService {

    @Getter
    private List<City> cities;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private CityRepository cityRepository;

    Logger logger = LoggerFactory.getLogger(CityService.class);


    public ResponseEntity<?> getCitiesInRadius(GetCitiesInRadiusRequest getCitiesInRadiusRequest, HttpServletRequest request){
        HashMap<String, User> response = userComponent.getUserFromRequest(request);
        if(!response.containsKey("OK")){
            String error = response.keySet().stream().findFirst().orElse("Unknown error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        User user = response.get("OK");

        City city = getCityByName(getCitiesInRadiusRequest.getCity());
        if (city == null) {
            logger.error("City '{}' not found in loaded cities", getCitiesInRadiusRequest.getCity());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("City not found");
        }


        List<City> citiesInRadius = getListOfCitiesInRadius(city, getCitiesInRadiusRequest.getRadius());

        if(citiesInRadius.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No cities found in radius");
        }
        return ResponseEntity.ok(citiesInRadius);

    }
    public List<City> getListOfCitiesInRadius(City city, Integer radius) {
        return cities.stream()
                .filter(c ->
                        calculateDistance(
                                city.getLatitude(), city.getLongitude(),
                                c.getLatitude(), c.getLongitude()) <= radius)
                .toList();
    }

    @PostConstruct
    public void init() {
        Gson gson = new Gson();
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/data.json"))) {
            cities = gson.fromJson(reader, new TypeToken<List<City>>() {}.getType());
            if (cities == null) {
                logger.error("Error while reading data.json");
                cities = List.of();
            } else {
                logger.info("Cities loaded successfully ");
            }

        } catch (IOException e) {
            logger.error("Error while reading data.json", e);
        }
    }


    public City getCityByName(String cityName) {
        return cities.stream()
                .filter(city -> city.getCity() != null && city.getCity().equalsIgnoreCase(cityName))
                .findFirst()
                .orElse(null);
    }



    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Promień Ziemi w kilometrach
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Odległość w kilometrach
    }


}
