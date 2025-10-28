public class Filter {
    private Level Level;
    private String preferredMajor;
    private String applicationOpenDate;
    private String applicationCloseDate;
    private CompanyRepresentative representative;
    private int numOfSlots;
    private Status Status;
    private boolean visibility;
    
    public Filter(Level Level, String preferredMajor, String applicationOpenDate, String applicationCloseDate, CompanyRepresentative representative, int numOfSlots, Status Status, boolean visibility) {
        this.Level = Level;
        this.preferredMajor = preferredMajor;
        this.applicationOpenDate = applicationOpenDate;
        this.applicationCloseDate = applicationCloseDate;
        this.representative = representative;
        this.numOfSlots = numOfSlots;
        this.Status = Status;
        this.visibility = visibility;
    }
    public Level getLevel() {
        return Level;
    }
    public String getPreferredMajor() {
        return preferredMajor;
    }
    public String getApplicationOpenDate() {
        return applicationOpenDate;
    }
    public String getApplicationCloseDate() {
        return applicationCloseDate;
    }
    public CompanyRepresentative getRepresentative() {
        return representative;
    }
    public int getNumOfSlots() {
        return numOfSlots;
    }
    public Status getStatus() {
        return Status;
    }
    public boolean isVisibility() {
        return visibility;
    }
}