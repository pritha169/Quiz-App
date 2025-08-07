package Algo_project;


public class User {
    private final String name;
    private final String contact; // This can be either  phone number

    public User(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}