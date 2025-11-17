
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
    public void addInterview(Interview interview) {
        String[] row = {
                interview.getStudentId(),
                interview.getInternshipId(),
                interview.getProposedTime(),
                interview.getConfirmedTime()
        };
        CSVUtil.appendRow(DataConfig.INTERVIEW_SCHEDULE_PATH, row);
    }

    @Override
    public void updateInterview(Interview updatedInterview) {
        CSVUtil.updateMatchingRows(
                DataConfig.INTERVIEW_SCHEDULE_PATH,
                r -> r.length >= 2 &&
                        r[0].equals(updatedInterview.getStudentId()) &&
                        r[1].equals(updatedInterview.getInternshipId()),
                r -> new String[] {
                        updatedInterview.getStudentId(),
                        updatedInterview.getInternshipId(),
                        updatedInterview.getProposedTime(),
                        updatedInterview.getConfirmedTime()
                });
    }

    // @Override
    // public Internship getInternshipById(String internshipId) {
    //     // Example implementation:
    //     List<Internship> allInternships = getAllInternships(); // or your method to fetch all
    //     for (Internship internship : allInternships) {
    //         if (internship.getInternshipId().equals(internshipId)) {
    //             return internship;
    //         }
    //     }
    //     return null;
    // }

    // @Override
    // public List<Interview> getInterviewsByCompanyRep(String companyRepId, List<Internship> allInternships) {
    //     List<Interview> all = getAllInterviews();
    //     List<Interview> result = new ArrayList<>();
    //     for (Interview i : all) {
    //         Internship internship = null;
    //         for (Internship temp : allInternships) {
    //             if (temp.getInternshipId().equals(i.getInternshipId())) {
    //                 internship = temp;
    //                 break;
    //             }
    //         }
    //         if (internship != null && internship.getRep() != null
    //             && internship.getRep().getUserId().equals(companyRepId)) {
    //             result.add(i);
    //         }
    //     }
    //     return result;
    // }
}