package rentalSpacePortfolio.scheduler;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rentalSpacePortfolio.repository.BookingRepository;


@Component
@Slf4j
public class BookingCleanupScheduler {
    
    private final BookingRepository bookingRepo;
    
    @Autowired
    public BookingCleanupScheduler(
            BookingRepository bookingRepo){
        this.bookingRepo = bookingRepo;
    }
    
    
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanupExpiredBookings(){
        LocalDateTime now = LocalDateTime.now();
        
        int updatedCountInBooking = bookingRepo.cancelExpiredBookings(now);
        
        if(updatedCountInBooking > 0){
            log.info("Cleaned up {} expired booking reservations.", updatedCountInBooking);
        }
    }
    

}