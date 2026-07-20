package rentalSpacePortfolio.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import rentalSpacePortfolio.entity.FlatImage;


public interface FlatImageRepository extends JpaRepository<FlatImage, UUID>{
    
}
