package rentalSpacePortfolio.scheduler;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rentalSpacePortfolio.entity.AmenityBooking;
import rentalSpacePortfolio.repository.AmenityBookingRepository;
import rentalSpacePortfolio.service.impl.EmailService;

@Component
@Slf4j
public class BookingAlertSchedular {
    
    private final AmenityBookingRepository amenityBookingRepo;
    private final EmailService emailService;
    
    @Autowired
    public BookingAlertSchedular(AmenityBookingRepository amenityBookingRepo, 
            EmailService emailService){
        this.amenityBookingRepo = amenityBookingRepo;
        this.emailService = emailService;
    }
    
    // MORNING ALERT - Runs every day exactly at 06:00 AM
    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void sendMorningBookingAlert() {
        LocalDateTime now  = LocalDateTime.now();
        
        log.info("Daily starting alert has been sended");
        
        LocalDateTime dayStart = now.with(LocalTime.MIN);
        LocalDateTime dayEnd = now.with(LocalTime.MAX);
        
        List<AmenityBooking> todaysBooking = amenityBookingRepo.findBookingsForDay(dayStart, dayEnd);
        
        for(AmenityBooking booking : todaysBooking){
            String userEmail = booking.getBooking().getTenant().getUser().getEmail();
            String formattedTime = booking.getStartAt().toLocalTime().toString();
            
            emailService.sendEmail(userEmail,
                "Reminder: You have a booking today!", 
                "Good morning! This is a reminder that you have booked the " + 
                        booking.getAmenity().getName() + " for today at " + formattedTime + "."
            );
            
            booking.setDayBookingAlertSent(true); 
            amenityBookingRepo.save(booking); 
        }
    }
    
    // 1 HOUR BEFORE ALERT: Run every 15 minutes
    @Scheduled(cron = "0 */15 * * * *")
    @Transactional
    public void sendHourlyReminders(){
        LocalDateTime now = LocalDateTime.now();
        
        log.info("Alert sent before booking start");
        
        LocalDateTime hourStart = now;
        LocalDateTime hourEnd = now.plusHours(1);
        List<AmenityBooking> hourAfterBookings = amenityBookingRepo.findBookingsForHourBeforeAlert(hourStart, hourEnd);
        
        for(AmenityBooking booking : hourAfterBookings){
            String userEmail = booking.getBooking().getTenant().getUser().getEmail();
            
            emailService.sendEmail(userEmail,
                "Urgent: Your booking starts in 1 hour!", 
                "Get ready! Your booking for " + booking.getAmenity().getName()
                        + " starts shortly at " + booking.getStartAt().toLocalTime() + "."
            );
            
            booking.setHourBeforeAlertSent(true); 
            amenityBookingRepo.save(booking); 
        }
    }
}