package rentalSpacePortfolio.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Amenity;


@Repository 
public interface AmenityRepository extends JpaRepository<Amenity, UUID>{
    
}
