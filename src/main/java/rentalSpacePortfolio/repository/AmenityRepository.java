package rentalSpacePortfolio.repository;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Amenity;


@Repository 
public interface AmenityRepository extends JpaRepository<Amenity, UUID>{
    
    @Query("SELECT a FROM Amenity a LEFT JOIN FETCH a.amenityImages WHERE a.property.id = :amenityId AND a.status = ACTIVE")
    Page<Amenity> findAllWithImages(@Param("amenityId") UUID amenityId, Pageable pageable);
    
}
