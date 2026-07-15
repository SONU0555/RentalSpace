package rentalSpacePortfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class testController {
    
    @PostMapping("/test")
    public ResponseEntity<String> getContentType(@RequestParam("file") MultipartFile file){
        String contentType = file.getContentType();
        return ResponseEntity.ok(contentType);
    }

}