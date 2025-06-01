package uk.techreturners.VirtuArt.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String email;
    private String name;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "google_id", unique = true)
    private String googleId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exhibition> exhibitions = new ArrayList<>();
}
