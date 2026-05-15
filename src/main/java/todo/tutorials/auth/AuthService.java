package todo.tutorials.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.tutorials.auth.dto.AuthResponse;
import todo.tutorials.auth.dto.LoginRequest;
import todo.tutorials.auth.dto.RegisterRequest;
import todo.tutorials.common.ApiException;
import todo.tutorials.model.MerchantProfile;
import todo.tutorials.model.Role;
import todo.tutorials.model.User;
import todo.tutorials.repository.MerchantProfileRepository;
import todo.tutorials.repository.UserRepository;
import todo.tutorials.service.AuditService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final MerchantProfileRepository merchantProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuditService auditService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Email is already registered");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ApiException("Invalid role. Use MERCHANT or CLIENT");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        User saved = userRepository.save(user);

        if (role == Role.MERCHANT) {
            MerchantProfile profile = new MerchantProfile();
            profile.setUser(saved);
            String businessName = request.getBusinessName() == null || request.getBusinessName().isBlank()
                    ? "Default Merchant"
                    : request.getBusinessName();
            profile.setBusinessName(businessName);
            merchantProfileRepository.save(profile);

            // Store merchant profile in Firebase
            Map<String, Object> merchantData = new HashMap<>();
            merchantData.put("userId", saved.getId());
            merchantData.put("name", request.getName());
            merchantData.put("email", saved.getEmail());
            merchantData.put("businessName", businessName);
            merchantData.put("role", role.name());
            merchantData.put("registeredAt", System.currentTimeMillis());
            auditService.storeUserProfile(saved.getId().toString(), merchantData);
        } else {
            // Store client profile in Firebase
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", saved.getId());
            userData.put("name", request.getName());
            userData.put("email", saved.getEmail());
            userData.put("role", role.name());
            userData.put("registeredAt", System.currentTimeMillis());
            auditService.storeUserProfile(saved.getId().toString(), userData);
        }

        // Log registration activity
        Map<String, Object> registrationDetails = new HashMap<>();
        registrationDetails.put("name", request.getName());
        registrationDetails.put("email", saved.getEmail());
        registrationDetails.put("role", role.name());
        auditService.logUserActivity(saved.getId().toString(), "USER_REGISTERED", registrationDetails);

        String token = jwtService.generateToken(saved);
        log.info("User registered successfully: {}", saved.getEmail());
        return new AuthResponse(token, saved.getEmail(), saved.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Invalid credentials"));

        // Log login activity
        Map<String, Object> loginDetails = new HashMap<>();
        loginDetails.put("email", user.getEmail());
        loginDetails.put("role", user.getRole().name());
        loginDetails.put("loginTime", System.currentTimeMillis());
        auditService.logUserActivity(user.getId().toString(), "USER_LOGIN", loginDetails);

        String token = jwtService.generateToken(user);
        log.info("User logged in successfully: {}", user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }
}
