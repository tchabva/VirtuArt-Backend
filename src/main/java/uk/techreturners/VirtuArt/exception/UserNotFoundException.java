package uk.techreturners.VirtuArt.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){super(message);}
}