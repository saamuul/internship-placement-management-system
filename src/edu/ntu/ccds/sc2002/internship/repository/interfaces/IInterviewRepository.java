
package edu.ntu.ccds.sc2002.internship.repository.interfaces;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.model.Internship;
import edu.ntu.ccds.sc2002.internship.model.Interview;

public interface IInterviewRepository {
    List<Interview> getAllInterviews();

    void addInterview(Interview interview);

    void updateInterview(Interview interview);

    // Internship getInternshipById(String internshipId);

    // List<Interview> getInterviewsByCompanyRep(String companyRepId, IInternshipRepository internshipRepository);
}
