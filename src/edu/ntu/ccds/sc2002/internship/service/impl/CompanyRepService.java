package edu.ntu.ccds.sc2002.internship.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.ntu.ccds.sc2002.internship.dto.OperationResult;
import edu.ntu.ccds.sc2002.internship.enums.Level;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.Interview;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IApplicationRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IInternshipRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IInterviewRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IUserRepository;
import edu.ntu.ccds.sc2002.internship.service.interfaces.ICompanyRepService;

/**
 * Implementation of ICompanyRepService.
 * Handles all company representative business logic.
 */
public class CompanyRepService implements ICompanyRepService {
    private final IUserRepository userRepository;
    private final IInternshipRepository internshipRepository;
    private final IApplicationRepository applicationRepository;
    private final IInterviewRepository interviewRepository;

    public CompanyRepService(IUserRepository userRepository,
            IInternshipRepository internshipRepository,
            IApplicationRepository applicationRepository,
            IInterviewRepository interviewRepository) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
    }

    @Override
    public OperationResult createOpportunity(String repId, String title, String description,
            Level level, String major, String openDate,
            String closeDate, int slots) {
        // Find the rep
        List<CompanyRepresentative> reps = userRepository.findAllCompanyReps();
        CompanyRepresentative rep = null;
        for (CompanyRepresentative r : reps) {
            if (r.getUserId().equalsIgnoreCase(repId)) {
                rep = r;
                break;
            }
        }

        if (rep == null) {
            return OperationResult.failure("Company representative not found.");
        }

        // Check max 5 opportunities limit
        List<InternshipOpportunity> existingOpps = getCreatedOpportunities(repId);
        if (existingOpps.size() >= 5) {
            return OperationResult.failure("Maximum number of allowed opportunities (5) reached.");
        }

        // Validate slots (max 10)
        if (slots < 1 || slots > 10) {
            return OperationResult.failure("Number of slots must be between 1 and 10.");
        }

        // Generate new ID
        String newId = generateNextOpportunityId();

        // Create opportunity
        InternshipOpportunity opportunity = new InternshipOpportunity(
                newId, title, description, major, openDate, closeDate, rep, slots, level);

        // Save
        boolean success = internshipRepository.save(opportunity);

        return success ? OperationResult.success("Internship opportunity created with ID: " + newId)
                : OperationResult.failure("Failed to create opportunity.");
    }

    @Override
    public List<InternshipOpportunity> getCreatedOpportunities(String repId) {
        // Find the rep's name first
        List<CompanyRepresentative> reps = userRepository.findAllCompanyReps();
        String repName = null;
        for (CompanyRepresentative r : reps) {
            if (r.getUserId().equalsIgnoreCase(repId)) {
                repName = r.getName();
                break;
            }
        }

        if (repName == null) {
            return new ArrayList<>(); // Rep not found
        }

        // Search by rep name (as stored in CSV)
        List<InternshipOpportunity> opportunities = internshipRepository.findByRepresentativeId(repName);

        // Sort by ID in ascending order
        opportunities.sort((o1, o2) -> {
            try {
                return Integer.compare(Integer.parseInt(o1.getInternshipID()), Integer.parseInt(o2.getInternshipID()));
            } catch (NumberFormatException e) {
                return o1.getInternshipID().compareToIgnoreCase(o2.getInternshipID());
            }
        });

        return opportunities;
    }

    @Override
    public OperationResult toggleVisibility(String repId, String opportunityId, boolean visible) {
        // Find the rep's name first
        List<CompanyRepresentative> reps = userRepository.findAllCompanyReps();
        String repName = null;
        for (CompanyRepresentative r : reps) {
            if (r.getUserId().equalsIgnoreCase(repId)) {
                repName = r.getName();
                break;
            }
        }

        if (repName == null) {
            return OperationResult.failure("Company representative not found.");
        }

        // Verify ownership
        Optional<InternshipOpportunity> oppOpt = internshipRepository.findById(opportunityId);
        if (!oppOpt.isPresent()) {
            return OperationResult.failure("Internship opportunity not found.");
        }

        InternshipOpportunity opp = oppOpt.get();
        if (!opp.getRep().getName().equalsIgnoreCase(repName)) {
            return OperationResult.failure("You don't have permission to modify this opportunity.");
        }

        boolean success = internshipRepository.updateVisibility(opportunityId, visible);
        String status = visible ? "visible" : "hidden";

        return success ? OperationResult.success("Internship opportunity is now " + status + ".")
                : OperationResult.failure("Failed to update visibility.");
    }

    @Override
    public List<InternshipApplication> getApplicationsForMyOpportunities(String repId) {
        List<InternshipApplication> myApplications = new ArrayList<>();

        // Get rep's opportunities using the same method as getCreatedOpportunities
        List<InternshipOpportunity> myOpportunities = getCreatedOpportunities(repId);

        for (InternshipOpportunity opp : myOpportunities) {
            List<InternshipApplication> apps = applicationRepository.findByInternshipId(opp.getInternshipID());
            myApplications.addAll(apps);
        }

        return myApplications;
    }

    @Override
    public List<InternshipApplication> getPendingApplications(String repId) {
        List<InternshipApplication> pendingApps = new ArrayList<>();
        List<InternshipApplication> allApps = getApplicationsForMyOpportunities(repId);

        for (InternshipApplication app : allApps) {
            if (app.getStatus() == Status.PENDING) {
                pendingApps.add(app);
            }
        }

        return pendingApps;
    }

    @Override
    public OperationResult reviewApplication(String repId, String applicationId, Status newStatus) {
        // Find application
        Optional<InternshipApplication> appOpt = applicationRepository.findById(applicationId);
        if (!appOpt.isPresent()) {
            return OperationResult.failure("Application not found.");
        }

        InternshipApplication app = appOpt.get();

        // Verify ownership - check if the internship is in rep's created opportunities
        List<InternshipOpportunity> myOpps = getCreatedOpportunities(repId);
        boolean isOwner = false;
        for (InternshipOpportunity myOpp : myOpps) {
            if (myOpp.getInternshipID().equals(app.getInternshipID())) {
                isOwner = true;
                break;
            }
        }

        if (!isOwner) {
            return OperationResult.failure("You don't have permission to review this application.");
        }

        // Get the internship opportunity for slot checking
        Optional<InternshipOpportunity> oppOpt = internshipRepository.findById(app.getInternshipID());
        if (!oppOpt.isPresent()) {
            return OperationResult.failure("Internship opportunity not found.");
        }

        InternshipOpportunity opp = oppOpt.get();

        // If approving (SUCCESSFUL), check and decrement slots
        if (newStatus == Status.SUCCESSFUL) {
            if (opp.getNumOfSlots() <= 0) {
                return OperationResult.failure("Cannot approve application. No slots remaining.");
            }

            // Decrement slot
            boolean slotDecremented = internshipRepository.decrementSlot(app.getInternshipID());
            if (!slotDecremented) {
                return OperationResult.failure("Failed to decrement slot. Opportunity may be full.");
            }
        }

        // Update application status
        boolean success = applicationRepository.updateStatus(applicationId, newStatus);

        return success ? OperationResult.success("Application reviewed successfully.")
                : OperationResult.failure("Failed to update application status.");
    }

    @Override
    public OperationResult changePassword(String repId, String oldPassword, String newPassword) {
        List<CompanyRepresentative> reps = userRepository.findAllCompanyReps();
        CompanyRepresentative rep = null;

        for (CompanyRepresentative r : reps) {
            if (r.getUserId().equalsIgnoreCase(repId)) {
                rep = r;
                break;
            }
        }

        if (rep == null) {
            return OperationResult.failure("Company representative not found.");
        }

        if (!rep.getPassword().equals(oldPassword)) {
            return OperationResult.failure("Old password is incorrect.");
        }

        rep.setPassword(newPassword);
        boolean success = userRepository.save(rep);

        return success ? OperationResult.success("Password changed successfully.")
                : OperationResult.failure("Failed to save password.");
    }

    private String generateNextOpportunityId() {
        List<InternshipOpportunity> all = internshipRepository.findAll();
        int maxId = 0;

        for (InternshipOpportunity opp : all) {
            try {
                int id = Integer.parseInt(opp.getInternshipID());
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }

        return String.valueOf(maxId + 1);
    }

    @Override
    public List<Interview> getProposedInterviews(String companyRepId) {
        // Get all interviews and filter by company rep's internships
        List<Interview> allInterviews = interviewRepository.getAllInterviews();
        List<InternshipOpportunity> myOpportunities = getCreatedOpportunities(companyRepId);
        List<Interview> result = new ArrayList<>();
        
        for (Interview interview : allInterviews) {
            // Check if interview is for one of my internships
            for (InternshipOpportunity opp : myOpportunities) {
                if (opp.getInternshipID().equals(interview.getInternshipId())) {
                    // Only show if proposed but not yet confirmed
                    if ((interview.getConfirmedTime() == null || interview.getConfirmedTime().isEmpty())
                        && interview.getProposedTime() != null && !interview.getProposedTime().isEmpty()) {
                        result.add(interview);
                    }
                    break;
                }
            }
        }
        return result;
    }
    
    @Override
    public List<Interview> getConfirmedInterviews(String companyRepId) {
        // Get all interviews and filter by company rep's internships
        List<Interview> allInterviews = interviewRepository.getAllInterviews();
        List<InternshipOpportunity> myOpportunities = getCreatedOpportunities(companyRepId);
        List<Interview> result = new ArrayList<>();
        
        for (Interview interview : allInterviews) {
            // Check if interview is for one of my internships
            for (InternshipOpportunity opp : myOpportunities) {
                if (opp.getInternshipID().equals(interview.getInternshipId())) {
                    // Only show if confirmed
                    if (interview.getConfirmedTime() != null && !interview.getConfirmedTime().isEmpty()) {
                        result.add(interview);
                    }
                    break;
                }
            }
        }
        return result;
    }
    
    @Override
    public boolean proposeInterview(String companyRepId, String internshipId, String studentId, String proposedTime) {
        // Verify ownership of the internship
        List<InternshipOpportunity> myOpps = getCreatedOpportunities(companyRepId);
        boolean isOwner = false;
        for (InternshipOpportunity opp : myOpps) {
            if (opp.getInternshipID().equals(internshipId)) {
                isOwner = true;
                break;
            }
        }
        
        if (!isOwner) {
            return false;
        }
        
        // Check if student has accepted this internship (status = SUCCESSFUL)
        List<InternshipApplication> allApps = applicationRepository.findAll();
        boolean hasAccepted = false;
        for (InternshipApplication app : allApps) {
            if (app.getStudentID().equals(studentId) && 
                app.getInternshipID().equals(internshipId) && 
                app.getStatus() == Status.SUCCESSFUL) {
                hasAccepted = true;
                break;
            }
        }
        
        if (!hasAccepted) {
            return false;
        }
        
        Interview interview = new Interview(studentId, internshipId, proposedTime, "");
        return interviewRepository.addInterview(interview);
    }

    @Override
    public boolean confirmInterview(String companyRepId, String internshipId, String studentId, String confirmedTime) {
        return interviewRepository.updateInterview(new Interview(studentId, internshipId, "", confirmedTime));
    }
    
    @Override
    public OperationResult editOpportunity(String repId, String opportunityId, String title, String description,
            Level level, String major, String openDate, String closeDate, int slots) {
        // Verify ownership
        Optional<InternshipOpportunity> oppOpt = internshipRepository.findById(opportunityId);
        if (!oppOpt.isPresent()) {
            return OperationResult.failure("Internship opportunity not found.");
        }
        
        InternshipOpportunity opp = oppOpt.get();
        
        // Find the rep's name
        List<CompanyRepresentative> reps = userRepository.findAllCompanyReps();
        String repName = null;
        for (CompanyRepresentative r : reps) {
            if (r.getUserId().equalsIgnoreCase(repId)) {
                repName = r.getName();
                break;
            }
        }
        
        if (repName == null) {
            return OperationResult.failure("Company representative not found.");
        }
        
        if (!opp.getRep().getName().equalsIgnoreCase(repName)) {
            return OperationResult.failure("You don't have permission to edit this opportunity.");
        }
        
        // Check if there are pending or approved applications
        List<InternshipApplication> applications = applicationRepository.findByInternshipId(opportunityId);
        for (InternshipApplication app : applications) {
            if (app.getStatus() == Status.PENDING || app.getStatus() == Status.SUCCESSFUL) {
                return OperationResult.failure("Cannot edit opportunity with pending or approved applications. Please review all applications first.");
            }
        }
        
        // Validate slots
        if (slots < 1 || slots > 10) {
            return OperationResult.failure("Number of slots must be between 1 and 10.");
        }
        
        // Update the opportunity
        opp.setTitle(title);
        opp.setDescription(description);
        opp.setLevel(level);
        opp.setPrefMajor(major);
        opp.setOpenDate(openDate);
        opp.setCloseDate(closeDate);
        opp.updateNumOfSlots(slots);
        
        boolean success = internshipRepository.save(opp);
        
        return success ? OperationResult.success("Internship opportunity updated successfully.")
                : OperationResult.failure("Failed to update opportunity.");
    }
    
    @Override
    public OperationResult deleteOpportunity(String repId, String opportunityId) {
        // Verify ownership
        Optional<InternshipOpportunity> oppOpt = internshipRepository.findById(opportunityId);
        if (!oppOpt.isPresent()) {
            return OperationResult.failure("Internship opportunity not found.");
        }
        
        InternshipOpportunity opp = oppOpt.get();
        
        // Find the rep's name
        List<CompanyRepresentative> reps = userRepository.findAllCompanyReps();
        String repName = null;
        for (CompanyRepresentative r : reps) {
            if (r.getUserId().equalsIgnoreCase(repId)) {
                repName = r.getName();
                break;
            }
        }
        
        if (repName == null) {
            return OperationResult.failure("Company representative not found.");
        }
        
        if (!opp.getRep().getName().equalsIgnoreCase(repName)) {
            return OperationResult.failure("You don't have permission to delete this opportunity.");
        }
        
        // Check if there are accepted applications or active interviews
        List<InternshipApplication> applications = applicationRepository.findByInternshipId(opportunityId);
        for (InternshipApplication app : applications) {
            if (app.getStatus() == Status.SUCCESSFUL) {
                return OperationResult.failure("Cannot delete opportunity with accepted applications. Students have already accepted this internship.");
            }
        }
        
        // Delete the opportunity
        boolean success = internshipRepository.deleteOpportunity(opportunityId);
        
        return success ? OperationResult.success("Internship opportunity deleted successfully.")
                : OperationResult.failure("Failed to delete opportunity.");
    }
}
