package uk.techreturners.VirtuArt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import uk.techreturners.VirtuArt.exception.UserNotFoundException;
import uk.techreturners.VirtuArt.model.User;
import uk.techreturners.VirtuArt.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User findOrCreateUser(Jwt jwt) {
        String googleId = jwt.getSubject();
        if (googleId == null || googleId.isBlank()) {
            throw new IllegalArgumentException("Google ID not found in JWT");
        }

        Optional<User> existingUser = userRepository.findByGoogleId(googleId);

        if (existingUser.isPresent()) {
            User upToDateUser = existingUser.get();
            boolean isUpdated = false;
            String emailFromJwt = jwt.getClaimAsString("email");
            String nameFromJwt = jwt.getClaimAsString("name");
            String pictureFromJwt = jwt.getClaimAsString("picture");

            if (emailFromJwt != null && !emailFromJwt.equals(upToDateUser.getEmail())) {
                upToDateUser.setEmail(emailFromJwt);
                isUpdated = true;
            }
            if (nameFromJwt != null && !nameFromJwt.equals(upToDateUser.getName())) {
                upToDateUser.setName(nameFromJwt);
                isUpdated = true;
            }
            if (pictureFromJwt != null && !pictureFromJwt.equals(upToDateUser.getProfilePicture())) {
                upToDateUser.setProfilePicture(pictureFromJwt);
                isUpdated = true;
            }
            if (isUpdated) {
                return userRepository.save(upToDateUser);
            }
            return upToDateUser;
        } else {
            User newUser = User.builder()
                    .googleId(googleId)
                    .email(jwt.getClaimAsString("email"))
                    .name(jwt.getClaimAsString("name"))
                    .profilePicture(jwt.getClaimAsString("picture"))
                    .build();
            return userRepository.save(newUser);
        }
    }

    @Override
    public User getCurrentUser(Jwt jwt) {
        User user = findOrCreateUser(jwt);
        if(user == null) {
            throw new UserNotFoundException("Could not obtain using the provided token");
        }
        return user;
    }
}