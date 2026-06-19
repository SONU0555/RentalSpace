
package rentalSpacePortfolio.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Admin;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>{
    
    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<Admin> findByUserId(@Param("userId") String userId);
    
}
