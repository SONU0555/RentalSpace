
package rentalSpacePortfolio.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID>{
    
//    @Query("SELECT p FROM Property p WHERE p.address = :address")
//    boolean existsByAddress(@Param("address") String address);
    
    boolean existsByAddress(String address);
}
