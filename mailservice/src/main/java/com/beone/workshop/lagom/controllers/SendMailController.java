package com.beone.workshop.lagom.controllers;

import com.beone.workshop.lagom.entities.StoredEMail;
import com.beone.workshop.lagom.model.SendMailRequestDto;
import com.beone.workshop.lagom.model.SendMailResponseDto;
import com.beone.workshop.lagom.repositories.StoredEMailRepository;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Controller for sending mails.
 */
@RestController
@RequestMapping("/api/send")
public class SendMailController {
  private  static final String ERROR_MESSAGE = "Please provide a request with at least one receipient, a subject and contents.";


  @Autowired private StoredEMailRepository repository;

  /**
   * api call to send a mail for other microservices.
   * @param request  mail sending request data.
   * @return response object with success indicator.
   */
  @RequestMapping(consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
  public @ResponseBody ResponseEntity<SendMailResponseDto> sendMail(@RequestBody final SendMailRequestDto request) {
    // perform consistency check on the request.
    if (isInvalidRequest(request)) {
      return new ResponseEntity<>(createResponse(false, ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
    }


    // create mail enry for each recipient.
    request.getRecipients().stream()
            .map(x -> createMail(x, request))
            .forEach(repository::save);

    return new ResponseEntity<>(createResponse(true, "ok"), HttpStatus.OK);
  }

  private StoredEMail createMail(final String recipient, final SendMailRequestDto request) {
    final StoredEMail result = new StoredEMail();

    result.setRecipient(recipient);
    result.setSubject(request.getSubject());
    result.setContents(request.getContents());
    result.setSentAt(LocalDateTime.now());

    return result;
  }

  private static SendMailResponseDto createResponse(final boolean success, final String message) {
    final SendMailResponseDto result = new SendMailResponseDto();
    result.setMessage(message);
    result.setSuccess(success);
    return result;
  }

  private static boolean isInvalidRequest(final SendMailRequestDto request) {
    return Strings.isNullOrEmpty(request.getContents())
            || Strings.isNullOrEmpty(request.getSubject())
            || request.getRecipients() == null
            || request.getRecipients().isEmpty();
  }

}
