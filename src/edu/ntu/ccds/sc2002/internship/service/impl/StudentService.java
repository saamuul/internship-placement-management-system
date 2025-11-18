package edu.ntu.ccds.sc2002.internship.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ntu.ccds.sc2002.internship.dto.OperationResult;
import edu.ntu.ccds.sc2002.internship.enums.Level;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.Internship;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.Interview;
import edu.ntu.ccds.sc2002.internship.model.Student;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IApplicationRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IInternshipRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IUserRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IWithdrawalRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IInterviewRepository;
import edu.ntu.ccds.sc2002.internship.service.interfaces.IStudentService;

/**
 * Implementation of IStudentService.
 * Handles all student-related business logic.
 */
public class StudentService implements IStudentService {
    private final IUserRepository userRepository;
    private final IInternshipRepository internshipRepository;
    private final IApplicationRepository applicationRepository;
    private final IWithdrawalRepository withdrawalRepository;
    private final IInterviewRepository interviewRepository;

    public StudentService(IUserRepository userRepository,
            IInternshipRepository internshipRepository,
            IApplicationRepository applicationRepository,
            IWithdrawalRepository withdrawalRepository,
            IInterviewRepository interviewRepository) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
        this.withdrawalRepository = withdrawalRepository;
        this.interviewRepository = interviewRepository;
    }

    @Override
    public List<Internship> getAvailableInternships(String studentId) {
        Optional<Student> studentOpt = userRepository.findStudentById(studentId);
        if (!studentOpt.isPresent()) {
            return new ArrayList<>();
        }

        Student student = studentOpt.get();
        List<Internship> internships = new ArrayList<>();
        List<InternshipOpportunity> allOpportunities = internshipRepository.findAll();

        for (InternshipOpportunity opp : allOpportunities) {
            // Filter 1: Check visibility
            if (!opp.getVisibility()) {
                continue;
            }

            // Filter 2: Check if status is SUCCESSFUL (approved by career staff)
            if (opp.getStatus() != Status.SUCCESSFUL) {
                continue;
            }

            // Filter 3: Check if major matches
            if (!opp.getPrefMajor().trim().equalsIgnoreCase(student.getMajor())) {
                continue;
            }

            // Filter 4: Check if student's year matches the level requirement
            Level level = opp.getLevel();
            if (student.getYearOfStudy() <= 2 && level != Level.BASIC) {
                continue;
            }

            String companyName = opp.getRep() != null ? opp.getRep().getName() : "";
            Internship internship = new Internship(opp.getInternshipID(), companyName,
                    opp.getTitle(), level, opp.getDescription(), opp.getOpenDate(),
                    opp.getCloseDate(), opp.getNumOfSlots());
            internships.add(internship);
        }

        // Sort by ID in ascending order
        internships.sort((i1, i2) -> {
            try {
                return Integer.compare(Integer.parseInt(i1.getInternshipId()), Integer.parseInt(i2.getInternshipId()));
            } catch (NumberFormatException e) {
                return i1.getInternshipId().compareToIgnoreCase(i2.getInternshipId());
            }
        });

        return internships;
    }

    @Override
    public OperationResult applyForInternship(String studentId, String internshipId) {
        // Check if student exists
        Optional<Student> studentOpt = userRepository.findStudentById(studentId);
        if (!studentOpt.isPresent()) {
            return OperationResult.failure("Student not found.");
        }

        Student student = studentOpt.get();

        // Check if student has accepted an internship already
        String acceptedId = userRepository.getAcceptedInternshipId(studentId);
        if (acceptedId != null && !acceptedId.isEmpty()) {
            return OperationResult.failure("Student have already accepted an internship placement.");
        }

        // Find the internship opportunity
        Optional<InternshipOpportunity> oppOpt = internshipRepository.findById(internshipId);
        if (!oppOpt.isPresent()) {
            return OperationResult.failure("Internship ID: " + internshipId + " not found.");
        }

        InternshipOpportunity opp = oppOpt.get();

        // Check visibility
        if (!opp.getVisibility()) {
            return OperationResult.failure("Internship ID: " + internshipId +
                    " is not visible or open for applications");
        }

        // Check major match
        if (!opp.getPrefMajor().trim().equalsIgnoreCase(student.getMajor())) {
            return OperationResult.failure("Student Major(" + student.getMajor() +
                    ") is not eligible for this internship");
        }

        // Check level eligibility
        if (student.getYearOfStudy() <= 2 && opp.getLevel() != Level.BASIC) {
            return OperationResult.failure("Year " + student.getYearOfStudy() +
                    " students can only apply for Basic-level internships.");
        }

        // Check if closing date has passed
        try {
            java.time.LocalDate closeDate = java.time.LocalDate.parse(opp.getCloseDate());
            java.time.LocalDate today = java.time.LocalDate.now();
            if (today.isAfter(closeDate)) {
                return OperationResult.failure("Application period has closed for this internship (closed on " +
                        opp.getCloseDate() + ").");
            }
        } catch (Exception e) {
            // If date parsing fails, skip this check
        }

        // Check if internship is filled
        if (opp.getStatus() == Status.FILLED) {
            return OperationResult.failure("This internship opportunity is already filled.");
        }

        // Check application limit (max 3 active applications)
        List<InternshipApplication> applications = applicationRepository.findByStudentId(studentId);
        int activeCount = 0;
        for (InternshipApplication app : applications) {
            if (app.getStatus() == Status.PENDING || app.getStatus() == Status.SUCCESSFUL) {
                activeCount++;
            }
        }

        if (activeCount >= 3) {
            return OperationResult.failure("You already have 3 active internship applications.");
        }

        // Check if already applied (and not rejected)
        for (InternshipApplication app : applications) {
            if (app.getInternshipID().equalsIgnoreCase(internshipId)) {
                if (app.getStatus() != Status.UNSUCCESSFUL) {
                    return OperationResult.failure("You have already applied for this internship (Status: " +
                            app.getStatus() + ").");
                }
            }
        }

        // Create new application
        String appId = applicationRepository.getNextApplicationId();
        InternshipApplication newApp = new InternshipApplication(appId, studentId, internshipId, Status.PENDING);
        applicationRepository.save(newApp);

        // Update student's applied internships list
        userRepository.addAppliedInternship(studentId, appId);

        return OperationResult.success(student.getName() + " successfully applied for internship ID: " + internshipId);
    }

    @Override
    public List<InternshipApplication> getApplications(String studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    @Override
    public OperationResult acceptInternship(String studentId, String applicationId) {
        // Find the application
        Optional<InternshipApplication> appOpt = applicationRepository.findById(applicationId);
        if (!appOpt.isPresent() || !appOpt.get().getStudentID().equalsIgnoreCase(studentId)) {
            return OperationResult.failure("Student have not applied with application ID: " + applicationId);
        }

        InternshipApplication app = appOpt.get();

        // Check status
        if (app.getStatus() != Status.SUCCESSFUL) {
            return OperationResult.failure("Only applications with status 'Successful' can be accepted.");
        }

        // Check if already accepted another
        InternshipApplication accepted = getAcceptedInternship(studentId);
        if (accepted != null) {
            return OperationResult.failure("Student have already accepted an internship placement.");
        }

        // Remove all other applications by this student
        applicationRepository.deleteByStudentExcept(studentId, applicationId);

        // Update student record - set accepted internship and clear applied internships
        userRepository.setAcceptedInternship(studentId, applicationId);

        // Check if all slots for this internship are now filled
        Optional<InternshipOpportunity> oppOpt = internshipRepository.findById(app.getInternshipID());
        if (oppOpt.isPresent()) {
            InternshipOpportunity opp = oppOpt.get();
            if (opp.getNumOfSlots() <= 0) {
                // All slots filled, update status to FILLED
                internshipRepository.updateStatus(app.getInternshipID(), Status.FILLED);
            }
        }

        return OperationResult.success("Internship accepted. Other applications have been withdrawn and removed.");
    }

    @Override
    public InternshipApplication getAcceptedInternship(String studentId) {
        String acceptedAppId = userRepository.getAcceptedInternshipId(studentId);

        if (acceptedAppId == null || acceptedAppId.trim().isEmpty()) {
            return null;
        }

        Optional<InternshipApplication> appOpt = applicationRepository.findById(acceptedAppId);
        return appOpt.isPresent() ? appOpt.get() : null;
    }

    @Override
    public OperationResult withdrawApplication(String studentId, String applicationId) {
        // Find the application
        Optional<InternshipApplication> appOpt = applicationRepository.findById(applicationId);
        if (!appOpt.isPresent() || !appOpt.get().getStudentID().equalsIgnoreCase(studentId)) {
            return OperationResult.failure("No application found with ID: " + applicationId + " for this student");
        }

        InternshipApplication app = appOpt.get();

        // Check if already unsuccessful
        if (app.getStatus() == Status.UNSUCCESSFUL) {
            return OperationResult.failure("This application is already " + app.getStatus() +
                    ". Student cannot withdraw.");
        }

        // Check if withdrawal request already exists
        if (withdrawalRepository.exists(applicationId, studentId)) {
            return OperationResult.failure("A withdrawal request is already pending for this application.");
        }

        // Create withdrawal request
        InternshipApplication withdrawal = new InternshipApplication(applicationId, studentId,
                app.getInternshipID(), Status.PENDING);
        withdrawalRepository.save(withdrawal);

        return OperationResult.success("Withdrawal request submitted for Application " + applicationId +
                ". Awaiting Approval.");
    }

    @Override
    public OperationResult changePassword(String studentId, String oldPassword, String newPassword) {
        Optional<Student> studentOpt = userRepository.findStudentById(studentId);
        if (!studentOpt.isPresent()) {
            return OperationResult.failure("Student not found.");
        }

        Student student = studentOpt.get();
        if (!student.getPassword().equals(oldPassword)) {
            return OperationResult.failure("Old password is incorrect.");
        }

        student.setPassword(newPassword);
        boolean success = userRepository.save(student);

        return success ? OperationResult.success("Password changed successfully.")
                : OperationResult.failure("Failed to save password.");
    }

    @Override
    public boolean proposeInterview(String studentId, String internshipId, String proposedTime) {
        Interview interview = new Interview(studentId, internshipId, proposedTime, "");
        return interviewRepository.addInterview(interview);
    }



    @Override
    public List<Interview> getStudentInterviews(String studentId) {
        List<Interview> all = interviewRepository.getAllInterviews();
        List<Interview> result = new ArrayList<>();
        for (Interview i : all) {
            if (i.getStudentId().equals(studentId)) result.add(i);
        }
        return result;
    }
}
