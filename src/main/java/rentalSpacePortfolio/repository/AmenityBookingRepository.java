package rentalSpacePortfolio.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rentalSpacePortfolio.entity.AmenityBooking;


public interface AmenityBookingRepository extends JpaRepository<AmenityBooking, UUID>{
    
    @Query("SELECT a FROM AmenityBooking a WHERE a.amenity.id = :amenityId AND a.startAt = :startAt AND a.endAt = :endAt")
    List<AmenityBooking> findAllBookedAmenities(@Param("amenityId") UUID amenityId,
           @Param("startAt") LocalDateTime startAt,
           @Param("endAt") LocalDateTime endAt
    );
    
}
