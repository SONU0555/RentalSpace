package rentalSpacePortfolio.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.FlatBooking;
import rentalSpacePortfolio.enums.BookingStatus;


@Repository
public interface FlatBookingRepository extends JpaRepository<FlatBooking, UUID>{
    
    List<FlatBooking> findByFlatId(UUID flatId);
    
    // Most important — overlap check for the SAME flat
    @Query("SELECT b FROM FlatBooking b WHERE b.flat.id = :flatId " +
           "AND b.booking.status IN :statuses " +
           "AND b.leaseStartDate < :endDate AND b.leaseEndDate > :startDate")
    List<FlatBooking> findOverlappingBookings(
        @Param("flatId") UUID flatId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("statuses") List<BookingStatus> statuses);
    
}
