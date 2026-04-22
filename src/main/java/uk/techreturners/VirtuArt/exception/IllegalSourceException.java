package uk.techreturners.VirtuArt.exception;

public class IllegalSourceException extends RuntimeException {
    public IllegalSourceException(String source) {
        super("Invalid data source: " + source);
    }
}
