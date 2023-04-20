package ru.pad.objects;

public class User {
    private String name, surname, birthDate, email, password, role, psychologist;

    private boolean requestAccepted;

    public User() {}

    public User(
            String name,
            String surname,
            String birthDate,
            String email,
            String password,
            String role,
            String psychologist,
            boolean requestAccepted
    ) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.role = role;
        this.psychologist = psychologist;
        this.requestAccepted = requestAccepted;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPsychologist() { return psychologist; }
    public void setPsychologist(String psychologist) { this.psychologist = psychologist; }

    public boolean getRequestAccepted() { return requestAccepted; }
    public void setRequestAccepted(boolean requestAccepted) { this.requestAccepted = requestAccepted; }
}