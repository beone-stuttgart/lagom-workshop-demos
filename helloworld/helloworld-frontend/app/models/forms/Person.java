package models.forms;

import play.data.validation.Constraints;


public class Person {
    @Constraints.Required
    private String firstName;
    @Constraints.Required
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
