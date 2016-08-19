package com.beone.workshop.lagom.repositories;

import com.beone.workshop.lagom.entities.StoredEMail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for accessing stored emails.
 */
public interface StoredEMailRepository extends CrudRepository<StoredEMail, Long> {

  /**
   * get all stored emails
   * @param pageable  paging information
   * @return page of stored emails.
   */
  Page<StoredEMail> findAll(Pageable pageable);

  /**
   * find emails by receipeient
   * @param receipient  receipient to query for
   * @param pageable    paging information for query
   * @return page of stored mails for a receipient.
   */
  @Query(value = "select u from StoredEMail u where u.recipient like %?1 order by u.sentAt desc",
    countQuery = "select count(*) from StoredEMail WHERE recipient like %?1")
  Page<StoredEMail> getEmails (String receipient, Pageable pageable);

}
