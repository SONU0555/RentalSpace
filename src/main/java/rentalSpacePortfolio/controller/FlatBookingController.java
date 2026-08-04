package rentalSpacePortfolio.controller;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.request.flat.FlatBookingRequest;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.dto.response.flat.FlatBookingResponse;
import rentalSpacePortfolio.service.impl.FlatBookingService;


@RestController
@RequestMapping(ApiPaths.BASE + "flat-bookings")
public class FlatBookingController {
    
    private final FlatBookingService bookingService;
    
    @Autowired
    public FlatBookingController(FlatBookingService bookingService){
        this.bookingService = bookingService;
    }
     
   @PostMapping
   @PreAuthorize("hasRole('TENANT')")
   public ResponseEntity<ApiResponse<FlatBookingResponse>> create(@RequestBody FlatBookingRequest req) {
       return ResponseEntity.ok(ApiResponse.success("Booking created successfully", bookingService.createBooking(req)));
   }

   @GetMapping("/{bookingId}")
   public ResponseEntity<ApiResponse<FlatBookingResponse>> getBookingById(@PathVariable UUID bookingId) {
       return ResponseEntity.ok(ApiResponse.success("Booking fetched successfully", bookingService.getBookingById(bookingId)));
   }

//    @GetMapping("/tenant/{tenantId}")
//    public ResponseEntity<List<FlatBookingResponse>> getByTenant(@PathVariable Long tenantId) {
//        return ResponseEntity.ok(bookingService.getBookingsByTenant(tenantId));
//    }
//
//    @PostMapping("/{id}/cancel")
//    public ResponseEntity<FlatBookingResponse> cancel(
//        @PathVariable Long id, @RequestBody CancelRequest req) {
//        return ResponseEntity.ok(bookingService.cancelBooking(id, req.getReason(), req.getCancelledBy()));
//    }

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