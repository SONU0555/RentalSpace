
package rentalSpacePortfolio.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Admin;


@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID>{
    
    @Query("SELECT a FROM Admin a WHERE a.property.id = :propertyId AND a.status = ASSIGNED")
    Admin findPropertyAdmin(@Param("propertyId") UUID propertyId);
    
}
