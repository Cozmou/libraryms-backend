package libraryms.repository.dto;

public record LoginRequest(
        String username, // Must be exactly lowercase "username"
        String password  // Must be exactly lowercase "password"
) {}