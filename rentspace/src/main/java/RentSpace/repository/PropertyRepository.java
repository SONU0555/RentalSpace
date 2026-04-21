
package RentSpace.repository;

import RentSpace.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PropertyRepository extends JpaRepository<Property, Long>{
    
}
