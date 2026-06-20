
package rentalSpacePortfolio.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Admin;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>{
    
    @Query("SELECT a FROM Admin a WHERE a.id = :userId")
    Optional<Admin> findByUserId(@Param("userId") UUID userId);
    
}
