package nl.tudelft.sem.resource.manager.domain.resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AssignedResourcesRepository extends JpaRepository<AssignedResources, Date> {
}
