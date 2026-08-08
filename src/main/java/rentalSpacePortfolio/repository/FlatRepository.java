package rentalSpacePortfolio.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Flat;

@Repository
public interface FlatRepository extends JpaRepository<Flat, UUID>{
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")}) // 3 seconds max wait
    @Query("SELECT f FROM Flat f WHERE f.id = :id")
    Optional<Flat> findByIdForUpdate(@Param("id") UUID id);
  
    @Query("SELECT f FROM Flat f WHERE f.property.id = :propertyId")
    List<Flat> findAllPropertyFlat(@Param("propertyId") UUID propertyId);
    

    @Query("SELECT f FROM Flat f LEFT JOIN FETCH f.flatImages WHERE f.property.id = :flatId AND f.status = VACANT")
    Page<Flat> findAllWithImages(@Param("flatId") UUID flatId, Pageable pageable);
    
}
