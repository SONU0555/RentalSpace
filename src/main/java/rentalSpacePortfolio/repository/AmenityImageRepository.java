package rentalSpacePortfolio.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.AmenityImage;

@Repository
public interface AmenityImageRepository extends JpaRepository<AmenityImage, UUID>{
    
}
