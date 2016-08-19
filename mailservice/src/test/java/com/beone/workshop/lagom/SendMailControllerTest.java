package com.beone.workshop.lagom;

import com.beone.workshop.lagom.controllers.SendMailController;
import com.beone.workshop.lagom.entities.StoredEMail;
import com.beone.workshop.lagom.model.SendMailRequestDto;
import com.beone.workshop.lagom.model.SendMailResponseDto;
import com.beone.workshop.lagom.repositories.StoredEMailRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SendMailControllerTest {

  private static final String RECEIPIENT_1_ADDRESS = "noreply@noreply.noreply";
  private static final String RECEIPIENT_2_ADDRESS = "other-user@noreply.noreply";
  private static final String SUBJECT = "subject of a mail";
  private static final String CONTENTS = "this is a test mail";

  @InjectMocks private SendMailController sut;
  @Mock private StoredEMailRepository repository;

  @Test
  public void sendMail_shouldUseRepository_toStoreMailTosendOut() {
    // fixture: create a request.
    final SendMailRequestDto request = prepareRequest(SUBJECT, CONTENTS, RECEIPIENT_1_ADDRESS, RECEIPIENT_2_ADDRESS);

    // and: expect the service call
    final List<String> receipients = new ArrayList<>();
    Mockito.when(repository.save(Mockito.any(StoredEMail.class)))
           .then(invocation -> {
             final StoredEMail email = (StoredEMail) invocation.getArguments()[0];
             receipients.add(email.getRecipient());
             assertThat(email.getContents()).describedAs("contents of mail").isEqualTo(CONTENTS);
             assertThat(email.getSubject()).describedAs("subject of mail").isEqualTo(SUBJECT);
             return email;
           });

    // execution: send the mail
    final ResponseEntity<SendMailResponseDto> result = sut.sendMail(request);

    // assertion: verify call to save method.
    Mockito.verify(repository, Mockito.times(2)).save(Mockito.any(StoredEMail.class));

    // and: two mails were sent to the correct addresses.
    assertThat(receipients).describedAs("sent mails to...").hasSize(2).contains(RECEIPIENT_1_ADDRESS, RECEIPIENT_2_ADDRESS);

    // and: should indicate success...
    assertThat(result.getStatusCode()).describedAs("status code").isEqualTo(HttpStatus.OK);
    assertThat(result.getBody().isSuccess()).describedAs("status flag in body").isTrue();
    assertThat(result.getBody().getMessage()).describedAs("status message").isEqualTo("ok");
  }

  @Test
  public void sendMail_shouldSendIllegalRequest_ifRequiredFieldsAreNotFilled() {
    // fixture: prepare illegal requests.
    final SendMailRequestDto noSubject = prepareRequest(null, "contents", "receipient");
    final SendMailRequestDto noContents = prepareRequest("subject", null, "receipient");
    final SendMailRequestDto noReceipients = prepareRequest("subject", "contents");

    // execution: call send with possible
    final ResponseEntity<SendMailResponseDto> noSubjectResult = sut.sendMail(noSubject);
    final ResponseEntity<SendMailResponseDto> noContentsResult = sut.sendMail(noContents);
    final ResponseEntity<SendMailResponseDto> noReceipientsResult = sut.sendMail(noReceipients);

    // assertion: did not send any mail at all.
    Mockito.verify(repository, Mockito.never()).save(Mockito.any(StoredEMail.class));

    // and: responded with error for all requests.
    assertThat(noSubjectResult.getStatusCode()).describedAs("no subject result status").isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(noContentsResult.getStatusCode()).describedAs("no contents result status").isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(noReceipientsResult.getStatusCode()).describedAs("no receipients result status").isEqualTo(HttpStatus.BAD_REQUEST);

    // and: did not set an ok status flag for any of the requests
    assertThat(noSubjectResult.getBody().isSuccess()).describedAs("success status of no subject request").isFalse();
    assertThat(noContentsResult.getBody().isSuccess()).describedAs("success status of no contents request").isFalse();
    assertThat(noReceipientsResult.getBody().isSuccess()).describedAs("success status of no receipients request").isFalse();
  }

  private static SendMailRequestDto prepareRequest(final String subject, final String contents, final String... receipients) {
    final SendMailRequestDto request = new SendMailRequestDto();

    request.setContents(contents);
    request.setSubject(subject);
    Stream.of(receipients).forEach(x -> request.getRecipients().add(x));

    return request;
  }

}