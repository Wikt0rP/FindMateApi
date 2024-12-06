package org.example.findmateapi.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.findmateapi.Entity.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserValidationResponse {
    private String status;
    private User user;




}

