package org.example.findmateapi.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.example.findmateapi.Entity.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class CityService {

    @Getter
    private List<City> cities;

    Logger logger = LoggerFactory.getLogger(CityService.class);


    @PostConstruct
    public void init(){
        Gson gson = new Gson();
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/data.json"))) {
            cities = gson.fromJson(reader, new TypeToken<>(){}.getType());
        } catch (IOException e) {
            logger.error("Error while reading data.json", e);
        }
    }

}
