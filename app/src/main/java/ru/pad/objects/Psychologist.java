package ru.pad.objects;

import java.util.Map;

public class Psychologist {
    private String name, surname, birthDate, email, password, role;

    private Map<String, String> requests;

    public Psychologist() {}

    public Psychologist(
            String name,
            String surname,
            String birthDate,
            String email,
            String password,
            String role,
            Map<String, String> requests
    ) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.role = role;
        this.requests = requests;
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

    public Map<String, String> getRequests() { return requests; }
    public void setRequests(Map<String, String> requests) { this.requests = requests; }
}