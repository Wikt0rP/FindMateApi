package org.example.findmateapi.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime createdDate;

    private Integer maxMembers;

    // 1st one is leader
    @ManyToMany(mappedBy = "teams")
    @JsonBackReference
    private List<User> users;



    public Team(String name, User user) {
        this.name = name;
        this.createdDate = LocalDateTime.now();
        //leader
        this.users.add(user);
    }

    public void addUser(User user){
        this.users.add(user);
    }



}
