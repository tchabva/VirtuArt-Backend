package uk.techreturners.VirtuArt.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiServiceException extends RuntimeException {
  private final HttpStatus status;

  public ApiServiceException(String message) {
    super(message);
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public ApiServiceException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}