package ru.pad.models;

public class User {
    private String name, surname, birthDate, email, password, role;

    public User() {}

    public User(
            String name,
            String surname,
            String birthDate,
            String email,
            String password,
            String role
    ) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String name) { this.surname = surname; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String name) { this.birthDate = birthDate; }

    public String getEmail() { return email; }
    public void setEmail(String name) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String name) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}