package models.forms;


import play.data.validation.Constraints;

public class Contribution {
    @Constraints.Required
    private String name;
    @Constraints.Required
    private String mail;
    @Constraints.Required
    private double amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
