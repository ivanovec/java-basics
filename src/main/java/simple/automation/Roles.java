package simple.automation;

public enum Roles{
    MANAGER("manager"),
    SENIOR_MANAGER("manager of managers"),
    LEAD_MANAGER("manager of manager of managers"),
    HARD_WORKER("just a worker");

    Roles(String description){
        this.description = description;
    }

    private String description;
    public String getDescription(){
        return description;
    }

    public void stubMethod() {
        // TODO: Implement stub method
    }

    public void anotherStubMethod() {
        // TODO: Implement another stub method
    }

    public void yetAnotherStubMethod() {
        // TODO: Implement yet another stub method
    }
}
