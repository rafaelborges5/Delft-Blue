package sem.faculty.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A DDD repository for querying and persisting user aggregate roots.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, String> {
}
