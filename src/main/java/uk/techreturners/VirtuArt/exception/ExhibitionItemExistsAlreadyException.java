package uk.techreturners.VirtuArt.exception;

import lombok.Getter;

@Getter
public class ExhibitionItemExistsAlreadyException extends RuntimeException {
    public ExhibitionItemExistsAlreadyException(String message) {
        super(message);
    }
}