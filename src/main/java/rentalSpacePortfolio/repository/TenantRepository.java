
package rentalSpacePortfolio.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long>{
    
    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<Tenant> findByUserId(@Param("userId") String userId);
}
