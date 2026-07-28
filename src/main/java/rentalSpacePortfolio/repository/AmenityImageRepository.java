package rentalSpacePortfolio.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rentalSpacePortfolio.entity.AmenityImage;
import rentalSpacePortfolio.entity.FlatImage;

@Repository
public interface AmenityImageRepository extends JpaRepository<AmenityImage, UUID>{
    
    @Query("SELECT i FROM AmenityImage i WHERE i.amenity.id = :amenityId")
    List<AmenityImage> findAllImagesByAmenityId(@Param("amenityId") UUID amenityId);
    
    @Query("SELECT i FROM AmenityImage i WHERE i.amenity.id = :amenityId AND i.imageDetails.displayOrder = :orderNum")
    AmenityImage findImageByDisplayOrder(
            @Param("amenityId") UUID amenityId,
            @Param("orderNum") Integer orderNum
    );
    
    @Modifying
    @Query("DELETE FROM AmenityImage i WHERE i.amenity.id = :amenityId AND i.imageDetails.displayOrder = :orderNum")
    int deleteImageByImageOrder(
            @Param("flatId") UUID flatId,
            @Param("orderNum") Integer orderNum
    );
    
}
