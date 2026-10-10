package rentalSpacePortfolio.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rentalSpacePortfolio.entity.AmenityBooking;
import rentalSpacePortfolio.enums.BookingStatus;


public interface AmenityBookingRepository extends JpaRepository<AmenityBooking, UUID>{
    
    @Query("SELECT COUNT(b) FROM AmenityBooking b WHERE b.amenity.id = :amenityId " +
       "AND b.booking.status IN :statuses " +
       "AND b.startAt < :endAt AND b.endAt > :startAt")
    int countOverlapping(@Param("amenityId") UUID amenityId,
           @Param("startAt") LocalDateTime startAt,
           @Param("endAt") LocalDateTime endAt,
           @Param("statuses") List<BookingStatus> statuses
    );
    
}
