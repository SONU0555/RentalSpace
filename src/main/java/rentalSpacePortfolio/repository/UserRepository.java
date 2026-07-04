
package rentalSpacePortfolio.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
    User findByEmail(String email);
    
    @Query("SELECT r FROM User r WHERE r.role = :role")
    User findOwnerByRole(@Param("role") Role role);
    
}
