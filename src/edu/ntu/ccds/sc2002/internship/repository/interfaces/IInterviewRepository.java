
package edu.ntu.ccds.sc2002.internship.repository.interfaces;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.model.Interview;

public interface IInterviewRepository {
    List<Interview> getAllInterviews();

    boolean addInterview(Interview interview);

    boolean updateInterview(Interview interview);
}
