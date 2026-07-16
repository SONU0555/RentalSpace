
package rentalSpacePortfolio.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID>{
    
//    @Query("SELECT p FROM Property p WHERE p.address = :address")
//    boolean existsByAddress(@Param("address") String address);
    
    @Query(value = "SELECT p FROM Property p LEFT JOIN FETCH p.propertyImages",
           countQuery = "SELECT COUNT(p) FROM Property p")
    Page<Property> findAllWithImages(Pageable pageable);
    
    boolean existsByAddress(String address);
}
