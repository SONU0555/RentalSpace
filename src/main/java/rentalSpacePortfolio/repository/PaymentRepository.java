package rentalSpacePortfolio.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID>{
    
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    
    Optional<Payment> findByOrderId(String orderId);

}
