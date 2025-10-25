package model;

public class InternshipOpportunity {
    private String title;
    private String description;
    private String prefMajor;
    private String opendate;
    private String closedate;
    private CompanyRepresentative rep;
    private int numofslots;
    private boolean visibility;
    private InternshipStatus status;
    private Level level;

    public InternshipOpportunity(String t, String descrip, String major, String startdate, String enddate,
            CompanyRepresentative r, int slots, boolean visible, Level l) {
        this.status = InternshipStatus.PENDING;
        this.title = t;
        this.description = descrip;
        this.prefMajor = major;
        this.opendate = startdate;
        this.closedate = enddate;
        this.rep = r;
        this.numofslots = slots;
        this.visibility = visible;
        this.level = l;
    }

    public void setVisibility(boolean set) {
        this.visibility = set;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPrefMajor() {
        return this.prefMajor;
    }

    public String getOpenDate() {
        return this.opendate;
    }

    public String getCloseDate() {
        return this.closedate;
    }

    public InternshipStatus getStatus() {
        return this.status;
    }

    public int getNumOfSlots() {
        return this.numofslots;
    }

    public boolean getVisibility() {
        return this.visibility;
    }
}