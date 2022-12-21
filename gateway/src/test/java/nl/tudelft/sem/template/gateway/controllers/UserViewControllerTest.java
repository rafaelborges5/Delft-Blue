package nl.tudelft.sem.template.gateway.controllers;

import nl.tudelft.sem.template.gateway.authentication.AuthManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import sem.commons.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class UserViewControllerTest {

    private ReplyingKafkaTemplate<String, FacultyNamePackageDTO, RegularUserView> resourceReplyingKafkaTemplateUserView;
    private ReplyingKafkaTemplate<String, DateDTO, SysadminUserView> replyingKafkaTemplateSysadminView;
    private AuthManager authManager;
    private UserViewController userViewController;

    private FacultyNameDTO facultyNameDTO;
    private Resource resource;
    private RegularUserView regularUserView;

    @BeforeEach
    void setUp() throws NotValidResourcesException {
        resourceReplyingKafkaTemplateUserView = Mockito.mock(ReplyingKafkaTemplate.class);
        replyingKafkaTemplateSysadminView = Mockito.mock(ReplyingKafkaTemplate.class);
        authManager = Mockito.mock(AuthManager.class);
        userViewController = new UserViewController(resourceReplyingKafkaTemplateUserView,
                replyingKafkaTemplateSysadminView, authManager);

        facultyNameDTO = new FacultyNameDTO("EEMCS");
        resource = new Resource(3, 2, 1);
        regularUserView = new RegularUserView(Map.of(facultyNameDTO, resource));
    }

    @Test
    void getNormalUserView() throws ExecutionException, InterruptedException, TimeoutException {
        RequestReplyFuture<String, FacultyNamePackageDTO, RegularUserView> future =
                Mockito.mock(RequestReplyFuture.class);
        ConsumerRecord<String, RegularUserView> consumerRecord =
                new ConsumerRecord<>("test", 1, 1L, "test",
                        regularUserView);
        Mockito.when(future.get(10, TimeUnit.SECONDS)).thenReturn(consumerRecord);
        Mockito.when(authManager.getFaculties()).thenReturn("[EEMCS, AE]");
        Mockito.when(resourceReplyingKafkaTemplateUserView.sendAndReceive(Mockito.any())).thenReturn(future);

        ResponseEntity<RegularUserView> responseEntity = userViewController.getNormalUserView();
        assertThat(responseEntity.getBody()).isEqualTo(regularUserView);
    }
}