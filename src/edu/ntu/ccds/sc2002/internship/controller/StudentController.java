package edu.ntu.ccds.sc2002.internship.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.dto.OperationResult;
import edu.ntu.ccds.sc2002.internship.model.Filter;
import edu.ntu.ccds.sc2002.internship.model.Internship;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.Interview;
import edu.ntu.ccds.sc2002.internship.model.Student;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.service.interfaces.IStudentService;
import edu.ntu.ccds.sc2002.internship.view.StudentView;

/**
 * Controller for Student-related operations.
 * CONTROLLER LAYER: Coordinates between View and Service.
 * - Receives input from View
 * - Calls Service methods
 * - Passes results to View for display
 * - Handles routing logic
 * DOES NOT print directly.
 * Uses Dependency Injection for service layer.
 */
public class StudentController {
    private final StudentView studentView;
    private final IStudentService studentService;

    private List<String> activityLog = new ArrayList<>();

    // Filter state persistence - maintains filter settings across menu pages
    private List<Internship> cachedInternships = null;
    private boolean filterApplied = false;

    public StudentController(StudentView studentView, IStudentService studentService) {
        this.studentView = studentView;
        this.studentService = studentService;
    }

    /**
     * Main controller method that handles student menu flow.
     * CONTROLLER: Handles routing, calls Model, tells View what to display.
     */
    public boolean handleStudentMenu(User user) {
        if (!(user instanceof Student)) {
            return false;
        }

        // View: Display menu and get choice
        studentView.showDashboard(user, activityLog);
        String choice = studentView.getMenuChoice();

        // Controller: Route based on choice
        switch (choice) {
            case "1": // View Available Internships
                handleViewInternships(user);
                break;

            case "2": // Filter Internships
                handleFilterInternships(user);
                break;

            case "3": // Clear Filters
                handleClearFilters(user);
                break;

            case "4": // Apply for Internship
                handleApplyForInternship(user);
                break;

            case "5": // View Internship Application(s)
                handleViewInternshipApplications(user);
                break;

            case "6": // View Accepted Internship
                handleViewAcceptedInternship(user);
                break;

            case "7": // Accept Internship
                handleAcceptInternship(user);
                break;

            case "8": // Withdraw Internship Application(s)
                handleWithdrawApplication(user);
                break;

            case "9": // Propose Interview
                handleInterviewProposal(user.getUserId());
                break;

            case "10": // Confirm Interview
                handleInterviewConfirmation(user.getUserId());
                break;

            case "11": // View Interviews
                handleShowStudentInterviews(user.getUserId());
                break;

            case "12": // Change Password
                return handleChangePassword(user);

            case "13": // Logout
                filterApplied = false;
                cachedInternships = null;
                activityLog.clear();
                studentView.showLogout();
                return true;

            default:
                studentView.showInvalidChoice();
        }

        return false;
    }

    // Handles viewing available internships.
    // Filter state is maintained - displays cached results if filter was applied
    private void handleViewInternships(User user) {
        String studentId = user.getUserId();

        // If no filter applied or cache is empty, fetch fresh data
        if (!filterApplied || cachedInternships == null) {
            cachedInternships = studentService.getAvailableInternships(studentId);
            filterApplied = false; // Mark as unfiltered fresh data
        }

        // Display cached internships (filtered or fresh)
        studentView.displayInternships(cachedInternships);

        // Optionally: prompt user if they want to apply/modify filters
        // For simplicity, filter persists until explicitly cleared or new data fetched
    }

    // Handles applying for an internship.
    private void handleApplyForInternship(User user) {
        handleViewInternships(user);

        // View: Get input
        String internshipId = studentView.getInternshipIdInput();

        // Service: Process application
        OperationResult result = studentService.applyForInternship(user.getUserId(), internshipId);

        // View: Display result
        if (result.isSuccess()) {
            activityLog.add("Applied for internship ID: " + internshipId);
            studentView.showSuccess(result.getMessage());
        } else {
            studentView.showError(result.getMessage());
        }
    }

    // Handles viewing student's internship applications.
    private void handleViewInternshipApplications(User user) {
        List<InternshipApplication> applications = studentService.getApplications(user.getUserId());

        // Check if there are any applications
        if (applications == null || applications.isEmpty()) {
            studentView.showError("No internship applications found.");
            return;
        }

        // Prepare data: Create lists for parallel display
        List<String> appIds = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> companies = new ArrayList<>();
        List<String> levels = new ArrayList<>();
        List<String> statuses = new ArrayList<>();

        // For each application, get the internship and extract data
        for (InternshipApplication application : applications) {
            Internship internship = application.getInternship();

            appIds.add(application.getApplicationID());
            statuses.add(application.getStatus().toString());

            if (internship != null) {
                titles.add(internship.getTitle());
                companies.add(internship.getCompanyRep());
                levels.add(internship.getLevel().toString());
            } else {
                titles.add("[Internship Not Found]");
                companies.add("N/A");
                levels.add("N/A");
            }
        }

        // Pass prepared data to view
        studentView.displayApplications(appIds, titles, companies, levels, statuses);
    }

    // Handles accepting an internship placement.
    private void handleAcceptInternship(User user) {
        handleViewInternshipApplications(user);

        // View: Get input
        String applicationId = studentView.getApplicationIdInput();

        // Service: Process acceptance
        OperationResult result = studentService.acceptInternship(user.getUserId(), applicationId);

        // View: Display result
        if (result.isSuccess()) {
            activityLog.add("Accepted internship application ID: " + applicationId);
            studentView.showSuccess(result.getMessage());
        } else {
            studentView.showError(result.getMessage());
        }
    }

    // Handles viewing accepted internship.
    private void handleViewAcceptedInternship(User user) {
        InternshipApplication acceptedApp = studentService.getAcceptedInternship(user.getUserId());
        studentView.displayAcceptedInternship(acceptedApp);
    }

    // Handles withdrawing an internship application.
    private void handleWithdrawApplication(User user) {
        handleViewInternshipApplications(user);

        // View: Get input
        String applicationId = studentView.getApplicationIdInputForWithdrawal();

        // Service: Process withdrawal
        OperationResult result = studentService.withdrawApplication(user.getUserId(), applicationId);

        // View: Display result
        if (result.isSuccess()) {
            activityLog.add("Withdrew internship application ID: " + applicationId);
            studentView.showSuccess(result.getMessage());
        } else {
            studentView.showError(result.getMessage());
        }
    }

    // Handles filtering internships based on user-selected criteria
    private void handleFilterInternships(User user) {
        String studentId = user.getUserId();

        // Get filter criteria from view
        Filter filter = studentView.getFilterInput();

        // Get all available internships first
        List<Internship> allInternships = studentService.getAvailableInternships(studentId);

        // Apply filter manually
        List<Internship> filteredInternships = new ArrayList<>();
        for (Internship internship : allInternships) {
            boolean matches = true;

            // Filter by level
            if (filter.getLevel() != null && internship.getLevel() != filter.getLevel()) {
                matches = false;
            }

            // Filter by company name (using repName field)
            if (filter.getRepName() != null &&
                    !internship.getCompanyRep().toLowerCase().contains(filter.getRepName().toLowerCase())) {
                matches = false;
            }

            if (filter.getClosingDate() != null && !filter.getClosingDate().isEmpty()) {
                LocalDate filterDate = LocalDate.parse(filter.getClosingDate());
                LocalDate internshipCloseDate = LocalDate.parse(internship.getCloseDate());
                if (internshipCloseDate.isAfter(filterDate)) {
                    continue; // Skip if closing date is after filter date
                }
            }

            if (matches) {
                filteredInternships.add(internship);
            }
        }

        // Cache the filtered results and mark filter as applied
        cachedInternships = filteredInternships;
        filterApplied = true;

        // Display filtered results
        studentView.displayInternships(cachedInternships);

        boolean anyFilterApplied = filter.getLevel() != null ||
                (filter.getRepName() != null && !filter.getRepName().isEmpty()) ||
                (filter.getClosingDate() != null && !filter.getClosingDate().isEmpty());

        if (anyFilterApplied) {
            studentView.showSuccess("Filter applied. Showing " + cachedInternships.size() + " internship(s).");
        } else {
            studentView.showSuccess("No filter applied. Showing all " + cachedInternships.size() + " internship(s).");
        }
    }

    // Handles clearing applied filters
    private void handleClearFilters(User user) {
        cachedInternships = null;
        filterApplied = false;
        studentView.showSuccess("Filters cleared. Viewing all available internships.");
        handleViewInternships(user);
    }

    // Handles password change.
    // Returns true if password changed successfully (to trigger logout), false
    // otherwise.
    private boolean handleChangePassword(User user) {
        // View: Get old password
        String oldPassword = studentView.getOldPasswordInput();

        // View: Get new password
        String newPassword = studentView.getNewPasswordInput();

        // View: Get password confirmation
        String confirmPassword = studentView.getConfirmPasswordInput();

        // Validate: Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            studentView.showError("Passwords do not match. Please try again.");
            return false; // Stay in menu
        }

        // Service: Change password
        OperationResult result = studentService.changePassword(user.getUserId(), oldPassword, newPassword);

        // View: Display result
        if (result.isSuccess()) {
            studentView.showSuccess(result.getMessage());
            return true; // Logout after successful password change
        } else {
            studentView.showError(result.getMessage());
            return false; // Stay in menu on error
        }
    }

    public void handleInterviewProposal(String studentId) {
        Interview interview = studentView.getInterviewProposalInput(studentId);
        // Check if interview already exists for this student and internship
        List<Interview> existingInterviews = studentService.getStudentInterviews(studentId);
        boolean alreadyProposed = false;
        for (Interview i : existingInterviews) {
            if (i.getInternshipId().equals(interview.getInternshipId())) {
                alreadyProposed = true;
                break;
            }
        }
        if (alreadyProposed) {
            studentView.showError("You have already proposed an interview for this internship.");
            return;
        }
        studentService.proposeInterview(interview.getStudentId(), interview.getInternshipId(),
                interview.getProposedTime());
        studentView.showSuccess("Interview proposed.");
    }

    public void handleInterviewConfirmation(String studentId) {
        Interview interview = studentView.getInterviewConfirmationInput(studentId);
        // Check if interview has been proposed for this internship
        List<Interview> existingInterviews = studentService.getStudentInterviews(studentId);
        Interview toConfirm = null;
        for (Interview i : existingInterviews) {
            if (i.getInternshipId().equals(interview.getInternshipId())) {
                toConfirm = i;
                break;
            }
        }
        if (toConfirm == null) {
            studentView.showError("You must propose an interview for this internship before confirming.");
            return;
        }
        if (toConfirm.getConfirmedTime() != null && !toConfirm.getConfirmedTime().isEmpty()) {
            studentView.showError("This interview has already been confirmed.");
            return;
        }
        studentService.confirmInterview(interview.getStudentId(), interview.getInternshipId(),
                interview.getConfirmedTime());
        studentView.showSuccess("Interview confirmed.");
    }

    public void handleShowStudentInterviews(String studentId) {
        List<Interview> interviews = studentService.getStudentInterviews(studentId);
        studentView.displayStudentInterviews(interviews);
    }
}