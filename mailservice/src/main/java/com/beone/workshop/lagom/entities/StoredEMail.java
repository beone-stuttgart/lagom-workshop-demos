package com.beone.workshop.lagom.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Implementation of a class that contains data for an email.
 */
@Entity
@Table(name = "StoredEMail")
public class StoredEMail implements Serializable {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 2048)
  private String recipient;

  @Column(nullable = false, length = 4096)
  private String subject;

  @Lob
  @Column(nullable = false, length = 131072)
  private String contents;

  @Column
  private LocalDateTime sentAt;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(final String recipient) {
    this.recipient = recipient;
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

  public LocalDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(final LocalDateTime sentAt) {
    this.sentAt = sentAt;
  }
}
