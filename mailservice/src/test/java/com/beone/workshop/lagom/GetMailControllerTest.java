package com.beone.workshop.lagom;

import com.beone.workshop.lagom.controllers.GetMailController;
import com.beone.workshop.lagom.entities.StoredEMail;
import com.beone.workshop.lagom.repositories.StoredEMailRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GetMailControllerTest {

  private static final String RECIPIENT_ADDRESS = "mailer-test@noreply.company";



  @InjectMocks private GetMailController sut;
  @Mock private StoredEMailRepository repository;


  @Test
  public void getMailsForAddress_shouldUseTheRepository_whenAdressRequested() {
    // fixture:
    final StoredEMail MAIL_1 = createStoredEMail(RECIPIENT_ADDRESS, "subject", "content", LocalDateTime.now());
    final StoredEMail MAIL_2 = createStoredEMail(RECIPIENT_ADDRESS, "subject", "content", LocalDateTime.now());

    Mockito.when(repository.getEmails(Mockito.eq(RECIPIENT_ADDRESS), Mockito.any(Pageable.class)))
           .then(invocation -> {
             final Pageable pagable = (Pageable) invocation.getArguments()[1];
             assertThat(pagable.getPageSize()).describedAs("page size").isEqualTo(200);
             assertThat(pagable.getOffset()).describedAs("page offset").isEqualTo(400);
             return new PageImpl<>(Arrays.asList(MAIL_1, MAIL_2), (Pageable) invocation.getArguments()[1], 2);
           });

    // execution: request mails to be sent out.
    final ResponseEntity<List<StoredEMail>> result = sut.getMailsForAddress(RECIPIENT_ADDRESS, 2);

    // assertion: verify by mock call.
    Mockito.verify(repository).getEmails(Mockito.eq(RECIPIENT_ADDRESS), Mockito.any(Pageable.class));
    assertThat(result.getStatusCode()).describedAs("status code").isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).describedAs("resulting mails queries").hasSize(2).contains(MAIL_1, MAIL_2);
  }

  private static StoredEMail createStoredEMail(final String recipient, final String subject, final String content,
          final LocalDateTime sentAt) {
    final StoredEMail result = new StoredEMail();

    result.setContents(content);
    result.setSubject(subject);
    result.setRecipient(recipient);
    result.setSentAt(sentAt);

    return result;
  }

}