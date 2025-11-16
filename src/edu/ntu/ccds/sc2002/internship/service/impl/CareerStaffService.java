package edu.ntu.ccds.sc2002.internship.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ntu.ccds.sc2002.internship.dto.OperationResult;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.CareerStaff;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.Filter;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.Report;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IApplicationRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IInternshipRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IUserRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IWithdrawalRepository;
import edu.ntu.ccds.sc2002.internship.service.interfaces.ICareerStaffService;

/**
 * Implementation of ICareerStaffService.
 * Handles all career staff business logic.
 */
public class CareerStaffService implements ICareerStaffService {
    private final IUserRepository userRepository;
    private final IInternshipRepository internshipRepository;
    private final IApplicationRepository applicationRepository;
    private final IWithdrawalRepository withdrawalRepository;

    public CareerStaffService(IUserRepository userRepository,
            IInternshipRepository internshipRepository,
            IApplicationRepository applicationRepository,
            IWithdrawalRepository withdrawalRepository) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
        this.withdrawalRepository = withdrawalRepository;
    }

    @Override
    public List<CompanyRepresentative> getPendingCompanyReps() {
        List<CompanyRepresentative> pending = new ArrayList<>();
        List<CompanyRepresentative> all = userRepository.findAllCompanyReps();

        for (CompanyRepresentative rep : all) {
            if (rep.getStatus() == Status.PENDING) {
                pending.add(rep);
            }
        }

        // Sort by ID in ascending order
        pending.sort((r1, r2) -> r1.getUserId().compareToIgnoreCase(r2.getUserId()));

        return pending;
    }

    @Override
    public OperationResult authorizeCompanyRep(String staffId, String repId, boolean approved) {
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

        Status newStatus = approved ? Status.SUCCESSFUL : Status.UNSUCCESSFUL;
        rep.setStatus(newStatus);

        boolean success = userRepository.save(rep);
        String action = approved ? "approved" : "rejected";

        return success ? OperationResult.success("Company representative " + action + " successfully.")
                : OperationResult.failure("Failed to update representative status.");
    }

    @Override
    public List<InternshipOpportunity> getAllOpportunities() {
        List<InternshipOpportunity> opportunities = internshipRepository.findAll();
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
    public List<InternshipOpportunity> getPendingOpportunities() {
        List<InternshipOpportunity> opportunities = internshipRepository.findByStatus(Status.PENDING);
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
    public OperationResult approveOpportunity(String staffId, String opportunityId, boolean approved) {
        Optional<InternshipOpportunity> oppOpt = internshipRepository.findById(opportunityId);
        if (!oppOpt.isPresent()) {
            return OperationResult.failure("Internship opportunity not found.");
        }

        Status newStatus = approved ? Status.SUCCESSFUL : Status.UNSUCCESSFUL;
        boolean success = internshipRepository.updateStatus(opportunityId, newStatus);
        String action = approved ? "approved" : "rejected";

        return success ? OperationResult.success("Internship opportunity " + action + " successfully.")
                : OperationResult.failure("Failed to update opportunity status.");
    }

    @Override
    public List<InternshipApplication> getWithdrawalRequests() {
        return withdrawalRepository.findAll();
    }

    @Override
    public List<InternshipApplication> getPendingWithdrawals() {
        return withdrawalRepository.findByStatus(Status.PENDING);
    }

    @Override
    public OperationResult approveWithdrawal(String staffId, String applicationId, boolean approved) {
        Optional<InternshipApplication> withdrawalOpt = withdrawalRepository.findById(applicationId);
        if (!withdrawalOpt.isPresent()) {
            return OperationResult.failure("Withdrawal request not found.");
        }

        InternshipApplication withdrawal = withdrawalOpt.get();

        if (approved) {
            // Check if the application being withdrawn was SUCCESSFUL (approved)
            Optional<InternshipApplication> appOpt = applicationRepository.findById(applicationId);
            if (appOpt.isPresent() && appOpt.get().getStatus() == Status.SUCCESSFUL) {
                // Increment slot back since approved application is being withdrawn
                internshipRepository.incrementSlot(withdrawal.getInternshipID());
            }

            // Update Student CSV to remove application references
            String studentId = withdrawal.getStudentID();
            userRepository.clearStudentInternships(studentId);

            // Approve: Delete the application and update withdrawal status
            applicationRepository.delete(applicationId);
            withdrawalRepository.updateStatus(applicationId, Status.SUCCESSFUL);
            return OperationResult.success("Withdrawal approved. Application removed.");
        } else {
            // Reject: Just update withdrawal status
            withdrawalRepository.updateStatus(applicationId, Status.UNSUCCESSFUL);
            return OperationResult.success("Withdrawal request rejected.");
        }
    }

    @Override
    public Report generateReport(String staffId, Filter filter) {
        // Find the staff member
        List<CareerStaff> staffList = userRepository.findAllCareerStaff();
        CareerStaff staff = null;

        for (CareerStaff s : staffList) {
            if (s.getUserId().equalsIgnoreCase(staffId)) {
                staff = s;
                break;
            }
        }

        // Generate report based on filter criteria
        List<InternshipOpportunity> opportunities = internshipRepository.findAll();

        Report report = new Report(filter, staff, opportunities);
        return report;
    }

    @Override
    public OperationResult changePassword(String staffId, String oldPassword, String newPassword) {
        List<CareerStaff> staffList = userRepository.findAllCareerStaff();
        CareerStaff staff = null;

        for (CareerStaff s : staffList) {
            if (s.getUserId().equalsIgnoreCase(staffId)) {
                staff = s;
                break;
            }
        }

        if (staff == null) {
            return OperationResult.failure("Career staff not found.");
        }

        if (!staff.getPassword().equals(oldPassword)) {
            return OperationResult.failure("Old password is incorrect.");
        }

        staff.setPassword(newPassword);
        boolean success = userRepository.save(staff);

        return success ? OperationResult.success("Password changed successfully.")
                : OperationResult.failure("Failed to save password.");
    }
}
