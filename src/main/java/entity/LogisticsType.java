package entity;

public enum LogisticsType {
    PEOPLE("PASSENGER"),
    GOODS("COMMERCIAL"),
    SPECIAL("SPECIAL");

    private final String requirement;

    LogisticsType(String requirement) {
        this.requirement = requirement;
    }

    public String getRequirement() {
        return requirement;
    }
}
