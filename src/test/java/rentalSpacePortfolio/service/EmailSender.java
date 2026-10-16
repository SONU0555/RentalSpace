package rentalSpacePortfolio.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rentalSpacePortfolio.repository.UserRepository;
import rentalSpacePortfolio.service.impl.EmailService;

@SpringBootTest
public class EmailSender {
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UserRepository userRepo;
    
    @Disabled
    @Test
    void testSendMail(){
        emailService.sendEmail("sonukumarr01.in@gmail.com",
                "Testing java mail sender",
                "This email server will be user for the RentalSpace project!");
    }
    
    @Test
    void isTenantExist(){
    }

}