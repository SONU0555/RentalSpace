package rentalSpacePortfolio.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.request.flat.BookingCancelRequest;
import rentalSpacePortfolio.dto.request.flat.FlatBookingRequest;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.dto.response.flat.FlatBookingResponse;
import rentalSpacePortfolio.security.SecurityUnits;
import rentalSpacePortfolio.service.impl.FlatBookingService;


@RestController
@RequestMapping(ApiPaths.BASE + "/bookings")
public class BookingController {
    
    private final FlatBookingService bookingService;
    
    @Autowired
    public BookingController(FlatBookingService bookingService){
        this.bookingService = bookingService;
    }
     
   @PostMapping("/flats")
   @PreAuthorize("hasRole('TENANT')")
   public ResponseEntity<ApiResponse<FlatBookingResponse>> createFlatBooking(@RequestBody FlatBookingRequest req) {
       return ResponseEntity.ok(ApiResponse.success("Booking created successfully", bookingService.createFlatBooking(req)));
   }

   @GetMapping("/{bookingId}")
   public ResponseEntity<ApiResponse<?>> getBookingById(@PathVariable UUID bookingId) {
       return ResponseEntity.ok(ApiResponse.success("Booking fetched successfully", bookingService.getBookingById(bookingId)));
   }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<ApiResponse<List<?>>> getByTenant(@PathVariable UUID tenantId) {
        List<FlatBookingResponse> response = bookingService.getBookingsByTenant(tenantId);
        return new ResponseEntity<>(new ApiResponse<>(true, "Booking fetched successfull by tenant ID", response), HttpStatus.OK);
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<String> cancel(
        @PathVariable UUID bookingId, @RequestBody BookingCancelRequest req) {
        
        String cancelledBy = SecurityUnits.getCurrentUserRole();
        bookingService.cancelBooking(bookingId, req.getCancellationReason(), cancelledBy);
        
        return new ResponseEntity<>("Booking cancelled successfully", HttpStatus.OK);
    }

    @PostMapping("/{id}/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> checkIn(
        @PathVariable UUID id, @RequestParam LocalDate date) {
        bookingService.markCheckIn(id, date);
        return ResponseEntity.ok("Successfully checked-In");
    }

    @PostMapping("/{id}/check-out")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> checkOut(
        @PathVariable UUID id, @RequestParam LocalDate date) {
        bookingService.markCheckOut(id, date);
        return ResponseEntity.ok("Successfully checked-out");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable UUID bookingId){
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.ok("Successfully deleted");
    }
}