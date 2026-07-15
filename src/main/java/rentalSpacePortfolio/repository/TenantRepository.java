
package rentalSpacePortfolio.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long>{
    
    @Query("SELECT t FROM Tenant t WHERE t.id = :userId")
    Optional<Tenant> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT t FROM Tenant t WHERE t.id = :propertyId")
    List<Tenant> findAllTenantByPropertyId(@Param("propertyId") Long propertyId);
}
