package com.beone.workshop.lagom.controllers;

import com.beone.workshop.lagom.entities.StoredEMail;
import com.beone.workshop.lagom.repositories.StoredEMailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for searching mail
 */
@RestController
@RequestMapping("/api/search")
public class GetMailController {

  @Autowired private StoredEMailRepository repository;

  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<List<StoredEMail>> getMailsForAddress(
          @RequestParam(name = "q", required = false, defaultValue = "") final String mail,
          @RequestParam(name = "page", required = false, defaultValue = "0") final int pageNo) {
    final Pageable pageable = new PageRequest(pageNo, 200);
    final Page<StoredEMail> page = repository.getEmails(mail, pageable);
    return new ResponseEntity<>(page.getContent(), HttpStatus.OK);
  }
}
