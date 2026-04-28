package RentSpace.controller.Private;

import RentSpace.requestDtos.User.BookingCreateRequestDto;
import RentSpace.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
public class BookingPrivateController {
    
    private final BookingService bookingService;
    
    @Autowired
    public BookingPrivateController(BookingService bookingService){
        this.bookingService = bookingService;
    }
    
    @PostMapping("/create")
    public ResponseEntity<String> createBooking(@Valid @RequestBody BookingCreateRequestDto request,
            @RequestParam Long tenantId,
            @RequestParam Long propertyId){
        bookingService.createNewBooking(request, tenantId, propertyId);
        
        return new ResponseEntity<>("Booking is pending", HttpStatus.ACCEPTED);
    }

}