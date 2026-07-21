package rentalSpacePortfolio.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.Flat;

@Repository
public interface FlatRepository extends JpaRepository<Flat, UUID>{
  
    @Query("SELECT f FROM Flat f WHERE f.property.id = :propertyId")
    List<Flat> findAllPropertyFlat(@Param("propertyId") UUID propertyId);
    

    @Query("SELECT f FROM Flat f LEFT JOIN FETCH f.flatImages WHERE f.property.id = :flatId AND f.isDeleted = false")
    Page<Flat> findAllWithImages(@Param("flatId") UUID flatId, Pageable pageable);
    
}
