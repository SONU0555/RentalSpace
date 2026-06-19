package rentalSpacePortfolio.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rentalSpacePortfolio.exception.UserNotFoundException;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.repository.UserRepository;



@Service
public class AdminService {
    
    public UserRepository userRepo;
    
    @Autowired
    public AdminService(UserRepository userRepo){
        this.userRepo = userRepo;
    }
    
    
    // Delete user
    public void deleteUser(Long id){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + id));
        userRepo.delete(user);
    }

}