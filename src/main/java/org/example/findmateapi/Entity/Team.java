package org.example.findmateapi.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Invitation> invitations;



    public Team(String name, User user) {
        this.name = name;
        this.createdDate = LocalDateTime.now();
        //leader
        this.users = new ArrayList<>();
        this.users.add(user);
    }

    public void addUser(User user){
        this.users.add(user);
    }



}
