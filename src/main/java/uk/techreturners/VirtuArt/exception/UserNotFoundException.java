package uk.techreturners.VirtuArt.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){
        super("Could not obtain a User using the provided token");
    }
}