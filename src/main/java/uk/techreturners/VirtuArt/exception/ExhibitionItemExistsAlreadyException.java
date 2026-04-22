package uk.techreturners.VirtuArt.exception;

import lombok.Getter;

@Getter
public class ExhibitionItemExistsAlreadyException extends RuntimeException {
    public ExhibitionItemExistsAlreadyException(String title, String id) {
        super(String.format("The ExhibitionItem name: %s and apiId: %s already in this Exhibition", title, id));
    }
}