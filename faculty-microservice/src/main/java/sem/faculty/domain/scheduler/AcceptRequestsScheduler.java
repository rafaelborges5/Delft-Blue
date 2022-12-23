package sem.faculty.domain.scheduler;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sem.commons.NotificationDTO;
import sem.faculty.controllers.ScheduleRequestController;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestRepository;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Strategy to mark requests as accepted and save them in the schedule in the faculty.
 */
@Service
public class AcceptRequestsScheduler extends SchedulableRequestsScheduler {

    public AcceptRequestsScheduler(ScheduleRequestController controller,
                                   RequestRepository requestRepository,
                                   KafkaTemplate<String, NotificationDTO> kafkaTemplate) {
        super(controller, requestRepository, kafkaTemplate);
    }

    @Override
    void saveRequestInFaculty(Request request, Faculty faculty, LocalDate date) {
        request.setStatus(RequestStatus.ACCEPTED);
        super.getKafkaTemplate().send("publish-notification", new NotificationDTO(request.getNetId(),
                        "Your request with name  " + request.getName() +
                                " has been accepted"));
        // update request repository
        long requestID = request.getRequestId();
        if (Objects.equals(requestRepository.findByRequestId(requestID), request)) {
            requestRepository.updateRequestStatusAccepted(requestID);
        } else {
            requestRepository.saveAndFlush(request);
        }

        //TODO: reserveResources(request, scheduledDate); //Reserve the resources for request on the scheduledDate.
        faculty.scheduleForDate(request, date);
    }
}
