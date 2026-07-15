package rentalSpacePortfolio.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.PropertyImage;

@Repository
public interface PropertyImageRepository extends JpaRepository<PropertyImage, UUID>{
    
    @Query("SELECT i FROM PropertyImage i WHERE i.property.id = :propertyId")
    List<PropertyImage> findAllImagesByPropertyId(@Param("propertyId") UUID propertyId);
    
    @Query("SELECT i FROM PropertyImage i WHERE i.property.id = :propertyId AND i.displayOrder = :orderNum")
    PropertyImage findImageByDisplayOrder(
            @Param("propertyId") UUID propertyId,
            @Param("orderNum") Integer orderNum
    );
    
    @Modifying
    @Query("DELETE FROM PropertyImage i WHERE i.property.id = :propertyId AND i.displayOrder = :orderNum")
    int deleteImageByImageOrder(
            @Param("propertyId") UUID propertyId,
            @Param("orderNum") Integer orderNum
    );
}
