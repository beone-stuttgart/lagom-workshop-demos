package com.beone.workshop.lagom.model;

/**
 * Response for sending message call.
 */
public class SendMailResponseDto {

  private String message;
  private boolean success;

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(final boolean success) {
    this.success = success;
  }
}
