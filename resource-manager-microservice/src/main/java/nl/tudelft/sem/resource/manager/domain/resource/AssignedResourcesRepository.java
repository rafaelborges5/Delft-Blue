package nl.tudelft.sem.resource.manager.domain.resource;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignedResourcesRepository extends JpaRepository<AssignedResources, Date> {
    Optional<AssignedResources> findByAssigneeAndDate(Assignee assignee, LocalDate date);
}
