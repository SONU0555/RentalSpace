package rentalSpacePortfolio.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Booking;


@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>{
    
//    @Query("SELECT b FROM Booking b WHERE b.id = :bookingId")
//    Optional<Booking> findByBookingId(UUID bookingId);
    
    @Query("SELECT b FROM Booking b WHERE b.bookingType = 'FLAT' AND b.tenant.id = :tenantId")
    List<Booking> findFlatByTenantId(@Param("tenantId") UUID tenantId);
    
    @Modifying
    @Query("UPDATE Booking b SET b.status = 'EXPIRED' WHERE b.status = 'PENDING' AND b.holdExpireAt < :currentTime")
    int cancelExpiredBookings(@Param("currentTime") LocalDateTime currentTime);

}