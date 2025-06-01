package uk.techreturners.VirtuArt.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uk.techreturners.VirtuArt.model.User;
import uk.techreturners.VirtuArt.repository.UserRepository;

import java.util.ArrayList;

@Component
public class TestDataInput implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        User user = User.builder()
                .email("mail@e.com")
                .googleId("test")
                .name("mail")
                .exhibitions(new ArrayList<>())
                .build();

        User savedUser = userRepository.save(user);
        System.out.println(savedUser.toString());
    }
}
