package nl.tudelft.sem.resource.manager.domain.resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Repository
public interface ReservedResourcesRepository extends JpaRepository<ReservedResources, Date> {
    Optional<ReservedResources> findByReserverAndDate(Reserver reserver, LocalDate date);
}
