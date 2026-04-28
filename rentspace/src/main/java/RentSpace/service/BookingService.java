package RentSpace.service;

import RentSpace.repository.BookingRepository;
import RentSpace.requestDtos.User.BookingCreateRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    
    private final BookingRepository bookingRepo;
    
    @Autowired
    public BookingService(BookingRepository bookingRepo){
        this.bookingRepo = bookingRepo;
    }
    
    // create tenent booking
    public void createNewBooking(BookingCreateRequestDto request, Long tenantId, Long propertyId){
        
    }

}