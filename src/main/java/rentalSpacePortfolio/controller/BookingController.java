package rentalSpacePortfolio.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.request.flat.BookingCancelRequest;
import rentalSpacePortfolio.dto.request.flat.FlatBookingRequest;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.dto.response.flat.FlatBookingResponse;
import rentalSpacePortfolio.security.SecurityUnits;
import rentalSpacePortfolio.service.impl.BookingService;


@RestController
@RequestMapping(ApiPaths.BASE + "/bookings")
public class BookingController {
    
    private final BookingService bookingService;
    
    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }
     
   @PostMapping("/flats")
   @PreAuthorize("hasRole('TENANT')")
   public ResponseEntity<ApiResponse<FlatBookingResponse>> createFlatBooking(@RequestBody FlatBookingRequest req) {
       return ResponseEntity.ok(ApiResponse.success("Booking created successfully", bookingService.createFlatBooking(req)));
   }

   @GetMapping("/flats/{bookingId}")
   public ResponseEntity<ApiResponse<FlatBookingResponse>> getBookingById(@PathVariable UUID bookingId) {
       return ResponseEntity.ok(ApiResponse.success("Booking fetched successfully", bookingService.getBookingById(bookingId)));
   }

    @GetMapping("/flats/tenant/{tenantId}")
    public ResponseEntity<ApiResponse<List<FlatBookingResponse>>> getByTenant(@PathVariable UUID tenantId) {
        List<FlatBookingResponse> response = bookingService.getBookingsByTenant(tenantId);
        return new ResponseEntity<>(new ApiResponse<>(true, "Booking fetched successfull by tenant ID", response), HttpStatus.OK);
    }

    @PostMapping("/flats/{bookingId}/cancel")
    public ResponseEntity<String> cancel(
        @PathVariable UUID bookingId, @RequestBody BookingCancelRequest req) {
        
        String cancelledBy = SecurityUnits.getCurrentUserRole();
        bookingService.cancelBooking(bookingId, req.getCancellationReason(), cancelledBy);
        
        return new ResponseEntity<>("Booking cancelled successfully", HttpStatus.OK);
    }

//    @PostMapping("/{id}/move-in")
//    public ResponseEntity<FlatBookingResponse> moveIn(
//        @PathVariable Long id, @RequestParam LocalDate date) {
//        return ResponseEntity.ok(bookingService.markMoveIn(id, date));
//    }
//
//    @PostMapping("/{id}/move-out")
//    public ResponseEntity<FlatBookingResponse> moveOut(
//        @PathVariable Long id, @RequestParam LocalDate date) {
//        return ResponseEntity.ok(bookingService.markMoveOut(id, date));
//    }

}