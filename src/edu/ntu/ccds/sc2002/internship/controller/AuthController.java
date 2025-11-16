package edu.ntu.ccds.sc2002.internship.controller;

import java.util.List;
import java.util.Optional;

import edu.ntu.ccds.sc2002.internship.dto.AuthResult;
import edu.ntu.ccds.sc2002.internship.model.Company;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.enums.RegistrationResult;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IUserRepository;

/**
 * Controller for authentication and user management.
 * CONTROLLER LAYER: Coordinates authentication flow between View and
 * Repository.
 * 
 * SOLID Principles Applied:
 * - Single Responsibility: Only handles authentication coordination
 * - Dependency Inversion: Depends on IUserRepository interface, not
 * implementation
 * - Uses Dependency Injection like other controllers
 * 
 * MVC Architecture:
 * - No direct CSV access (delegates to Repository layer)
 * - No business logic (just coordination)
 * - No view responsibilities
 */
public class AuthController {
    private final IUserRepository userRepository;

    public AuthController(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticates a user with userId and password.
     * CONTROLLER: Delegates to repository, coordinates authentication flow.
     */
    public AuthResult login(String userId, String password) {
        // Repository: Find user by ID or email
        Optional<User> userOpt = userRepository.findById(userId);

        if (!userOpt.isPresent()) {
            return new AuthResult(false, "Invalid user ID.", null);
        }

        User user = userOpt.get();

        // Model: Verify password using User's login method
        if (!user.login(userId, password)) {
            return new AuthResult(false, "Incorrect password.", null);
        }

        // Check if CompanyRep is approved
        if (user instanceof CompanyRepresentative) {
            CompanyRepresentative rep = (CompanyRepresentative) user;
            if (rep.getStatus() != Status.SUCCESSFUL) {
                return new AuthResult(false, "Account pending approval.", null);
            }
        }

        return new AuthResult(true, "Login successful.", user);
    }

    /**
     * Registers a new company representative.
     * CONTROLLER: Coordinates registration flow using repository.
     */
    public RegistrationResult registerCompanyRep(String id, String name, String email,
            String companyName, String dept, String pos) {
        // Check if already exists by email
        List<CompanyRepresentative> existingReps = userRepository.findAllCompanyReps();
        for (CompanyRepresentative rep : existingReps) {
            if (rep.getEmail().equalsIgnoreCase(email) ||
                    rep.getUserId().equalsIgnoreCase(email)) {
                return RegistrationResult.ALREADY_EXISTS;
            }
        }

        // Create new rep with PENDING status
        int nextId = existingReps.size() + 1;
        Company company = new Company(companyName, nextId);
        CompanyRepresentative newRep = new CompanyRepresentative(
                email, // Use email as userId for login
                name,
                email,
                "password", // Default password
                company,
                dept,
                pos);
        newRep.setStatus(Status.PENDING);

        // Repository: Save new representative
        boolean saved = userRepository.save(newRep);

        return saved ? RegistrationResult.SUCCESS : RegistrationResult.FAILED;
    }
}
