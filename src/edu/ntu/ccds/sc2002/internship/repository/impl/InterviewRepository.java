
package edu.ntu.ccds.sc2002.internship.repository.impl;

import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.config.DataConfig;
import edu.ntu.ccds.sc2002.internship.model.Interview;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IInterviewRepository;
import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

public class InterviewRepository implements IInterviewRepository {

    @Override
    public List<Interview> getAllInterviews() {
        List<Interview> interviews = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.INTERVIEW_SCHEDULE_PATH);
        for (String[] row : rows) {
            if (row.length < 4)
                continue;
            // Skip header if present
            if (row[0].equalsIgnoreCase("studentId"))
                continue;
            interviews.add(new Interview(row[0], row[1], row[2], row[3]));
        }
        return interviews;
    }

    @Override
    public boolean addInterview(Interview interview) {
        // Check if interview already exists for this student and internship
        List<Interview> existing = getAllInterviews();
        for (Interview i : existing) {
            if (i.getStudentId().equals(interview.getStudentId()) &&
                i.getInternshipId().equals(interview.getInternshipId())) {
                // Interview already exists, don't add duplicate
                return false;
            }
        }
        
        String[] row = {
                interview.getStudentId(),
                interview.getInternshipId(),
                interview.getProposedTime(),
                interview.getConfirmedTime()
        };
        return CSVUtil.appendRow(DataConfig.INTERVIEW_SCHEDULE_PATH, row);
    }

    @Override
    public boolean updateInterview(Interview updatedInterview) {
        int updated = CSVUtil.updateMatchingRows(
                DataConfig.INTERVIEW_SCHEDULE_PATH,
                r -> r.length >= 2 &&
                        r[0].equals(updatedInterview.getStudentId()) &&
                        r[1].equals(updatedInterview.getInternshipId()),
                r -> {
                    // Preserve existing values if new ones are empty
                    String proposedTime = updatedInterview.getProposedTime() != null && 
                                         !updatedInterview.getProposedTime().isEmpty() 
                                         ? updatedInterview.getProposedTime() 
                                         : r[2];
                    String confirmedTime = updatedInterview.getConfirmedTime() != null && 
                                          !updatedInterview.getConfirmedTime().isEmpty() 
                                          ? updatedInterview.getConfirmedTime() 
                                          : r[3];
                    return new String[] {
                        updatedInterview.getStudentId(),
                        updatedInterview.getInternshipId(),
                        proposedTime,
                        confirmedTime
                    };
                });
        return updated > 0;
    }
}