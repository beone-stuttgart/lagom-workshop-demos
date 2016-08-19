package com.beone.workshop.lagom.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Request datastructure when a mail is sent to our service.
 */
public class SendMailRequestDto implements Serializable {

  private List<String> recipients = new ArrayList<>();
  private String subject;
  private String contents;

  public List<String> getRecipients() {
    return recipients;
  }

  public void setRecipients(final List<String> recipients) {
    this.recipients = recipients;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(final String subject) {
    this.subject = subject;
  }

  public String getContents() {
    return contents;
  }

  public void setContents(final String contents) {
    this.contents = contents;
  }
}
