package simple.automation;

public enum Roles1 {
    MANAGER("mana5ger"),
    SENIOR_MANAGER("manager of m3anagers"),
    LEAD_MANAGER("manager of manager of 4managers"),
    HARD_WORKER("just a worke3r");

    Roles1(String description) {
        this.description = description;
    }

    private String description;
    private String art;
    private String boo;
    private String coo;

    public String getDescription() {
        return description;
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
