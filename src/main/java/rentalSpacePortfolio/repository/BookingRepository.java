package rentalSpacePortfolio.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Booking;
import rentalSpacePortfolio.enums.BookingStatus;


@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>{
    
//    @Query("SELECT b FROM Booking b WHERE b.id = :bookingId")
//    Optional<Booking> findByBookingId(UUID bookingId);
    
    @Query("SELECT b FROM Booking b WHERE b.bookingType = 'FLAT' AND b.tenant.id = :tenantId")
    List<Booking> findFlatByTenantId(@Param("tenantId") UUID tenantId);
    
    @Query("SELECT b FROM Booking b WHERE b.bookingType = 'AMENITY'"
            + " AND b.status IN ('PENDING', 'CONFIRMED')"
            + " AND b.amenityBooking.startAt = :dayBookingStartAt"
            + " AND b.tenant.id = :tenantId")
    List<Booking> findAmenityByTenantId(@Param("tenantId") UUID tenantId,
            @Param("dayBookingStartAt") LocalDateTime dayBookingStartAt);
    
    @Query("SELECT b FROM Booking b WHERE b.bookingType = 'AMENITY' AND b.tenant.id = :tenantId AND b.status = :filter")
    List<Booking> filterAndFindAmenityBooking(@Param("tenantId") UUID tenantId, @Param("filter") BookingStatus filter);
    
    List<Booking> findByTenantId(UUID tenantId);
    
    @Modifying
    @Query("UPDATE Booking b SET b.status = 'EXPIRED' WHERE b.status = 'PENDING' AND b.holdExpireAt < :currentTime")
    int cancelExpiredBookings(@Param("currentTime") LocalDateTime currentTime);
    
    @Modifying
    @Query("UPDATE Booking b SET b.status = 'COMPLETED' WHERE b.status = 'CONFIRMED' AND b.amenityBooking.endAt <= :currentTime")
    int updateCompletedBookings(@Param("currentTime") LocalDateTime currentTime);
    
}