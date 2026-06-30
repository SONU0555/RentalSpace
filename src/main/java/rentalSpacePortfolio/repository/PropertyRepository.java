
package rentalSpacePortfolio.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>{
    
    @Query("SELECT p FROM Property p WHERE p.address = :address")
    Property existsByAddress(@Param("address") String address);
    
    @Query("SELECT p FROM Property p WHERE p.id = :propertyId")
    Property findByPropertyId(@Param("propertyId") UUID propertyId);
    
}
