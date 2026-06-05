package libraryms.controller;

import libraryms.repository.dto.LoginRequest;
import libraryms.repository.dto.LoginResponse;
import libraryms.repository.AdminRepository;
import libraryms.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AdminRepository adminRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return adminRepository.findByUsername(req.username())
                .filter(admin -> admin.getPassword().equals(req.password()))
                .map(admin -> {
                    String token = jwtService.generateToken(admin.getUsername());
                    return ResponseEntity.ok(new LoginResponse(token, admin.getName()));
                })
                .orElse(ResponseEntity.status(401).build());
    }
}