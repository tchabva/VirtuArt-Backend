package uk.techreturners.VirtuArt.service;

import org.springframework.security.oauth2.jwt.Jwt;
import uk.techreturners.VirtuArt.model.User;

import java.util.Optional;

public interface UserService {
    User findOrCreateUser(Jwt jwt);

    Optional<User> findByGoogleId(String google);

    User getCurrentUser(Jwt jwt);
}