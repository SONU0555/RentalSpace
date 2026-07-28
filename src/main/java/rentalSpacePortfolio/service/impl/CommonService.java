package rentalSpacePortfolio.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.dto.response.tenant.TenantSummaryResponse;
import rentalSpacePortfolio.entity.AmenityImage;
import rentalSpacePortfolio.entity.BelongingImage;
import rentalSpacePortfolio.entity.Tenant;
import rentalSpacePortfolio.mapper.UserResponseMapper;
import rentalSpacePortfolio.repository.AdminRepository;
import rentalSpacePortfolio.repository.TenantRepository;
import rentalSpacePortfolio.repository.UserRepository;
import rentalSpacePortfolio.service.interfaces.ImageStorageService;

@Slf4j
@Service
public class CommonService {
        
    private final ImageStorageService imageStorageService;
    private final UserRepository userRepo;
    private final TenantRepository tenantRepo;
    private final AdminRepository adminRepo;
    public final PasswordEncoder passwordEncoder;
    
    @Autowired
    public CommonService(UserRepository userRepo,
            TenantRepository tenantRepo,
            AdminRepository adminRepo,
            PasswordEncoder passwordEncoder,
            ImageStorageService imageStorageService){
        this.userRepo = userRepo;
        this.tenantRepo = tenantRepo;
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.imageStorageService = imageStorageService;
    }
    
//     Service to get tenant by Id
    public TenantSummaryResponse getTenantById(UUID tenantId){
        Tenant tenant = tenantRepo.findByUserId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("User tenant not found with Id: " + tenantId));
        
        return UserResponseMapper.mapToTenatResponseDto(tenant);
    }
    
    // Service to store image to local disk and map to entity
    public <T, I extends BelongingImage<T>> List<I> mapAndSaveImage(
                List<MultipartFile> images,
                List<ImageRequest> imageRequests,
                T parentEntity,
                String folderName,
                Supplier<I> imageSupplier,
                JpaRepository<I, ?> repository) throws IOException{
    
        List<I> processedImageList = new ArrayList<>();
        
        for(int i = 0; i < images.size(); i++){
            MultipartFile file = images.get(i); // get actual image file
            ImageRequest req = imageRequests.get(i); // get image details
            
            // save image to disk, get back URL
            String imageUrl = imageStorageService.upload(file, folderName);
            
            I image = imageSupplier.get();
            image.getImageDetails().setImageUrl(imageUrl);
            image.getImageDetails().setDisplayOrder(req.getDisplayOrder());
            image.getImageDetails().setIsCoverImage(req.getIsCoverImage());
            image.setParent(parentEntity);
 
            processedImageList.add(image);
        }
                
        return repository.saveAll(processedImageList);    
    }
    
}