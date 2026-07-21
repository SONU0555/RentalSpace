package rentalSpacePortfolio.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rentalSpacePortfolio.entity.FlatImage;
import rentalSpacePortfolio.entity.PropertyImage;


public interface FlatImageRepository extends JpaRepository<FlatImage, UUID>{
    
    @Query("SELECT i FROM FlatImage i WHERE i.flat.id = :flatId")
    List<FlatImage> findAllImagesByFlatId(@Param("flatId") UUID flatId);
    
    @Query("SELECT i FROM FlatImage i WHERE i.flat.id = :flatId AND i.imageDetails.displayOrder = :orderNum")
    FlatImage findImageByDisplayOrder(
            @Param("flatId") UUID flatId,
            @Param("orderNum") Integer orderNum
    );
    
    @Modifying
    @Query("DELETE FROM FlatImage i WHERE i.flat.id = :flatId AND i.imageDetails.displayOrder = :orderNum")
    int deleteImageByImageOrder(
            @Param("flatId") UUID flatId,
            @Param("orderNum") Integer orderNum
    );
    
}
