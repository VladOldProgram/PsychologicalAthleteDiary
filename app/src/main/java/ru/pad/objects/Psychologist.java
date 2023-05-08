package ru.pad.objects;

import java.util.Map;

/**
 * Класс объекта психолога со свойствами:
 * <b>name</b>, <b>surname</b>, <b>birthDate</b>, <b>email</b>, <b>password</b>, <b>role</b>,
 * <b>requests</b>, <b>sportsmen</b>
 */
public class Psychologist {
    private String name, surname, birthDate, email, password, role;

    private Map<String, String> requests, sportsmen;

    /**
     * Конструктор - создание нового объекта с неинициализированными свойствами
     */
    public Psychologist() {}

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param name имя психолога
     * @param surname фамилия психолога
     * @param birthDate дата рождения психолога
     * @param email email психолога
     * @param password пароль психолога
     * @param role роль пользователя (психолог)
     * @param requests заявки спортсменов психологу
     * @param sportsmen спортсмены психолога
     */
    public Psychologist(
            String name,
            String surname,
            String birthDate,
            String email,
            String password,
            String role,
            Map<String, String> requests,
            Map<String, String> sportsmen
    ) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.role = role;
        this.requests = requests;
        this.sportsmen = sportsmen;
    }

    /**
     * Геттер свойства name
     * @return name - имя психолога
     */
    public String getName() { return name; }
    /**
     * Сеттер свойства name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Геттер свойства surname
     * @return surname - фамилия психолога
     */
    public String getSurname() { return surname; }
    /**
     * Сеттер свойства surname
     */
    public void setSurname(String surname) { this.surname = surname; }

    /**
     * Геттер свойства birthDate
     * @return birthDate - дата рождения психолога
     */
    public String getBirthDate() { return birthDate; }
    /**
     * Сеттер свойства birthDate
     */
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    /**
     * Геттер свойства email
     * @return email - email психолога
     */
    public String getEmail() { return email; }
    /**
     * Сеттер свойства email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Геттер свойства password
     * @return password - пароль психолога
     */
    public String getPassword() { return password; }
    /**
     * Сеттер свойства password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Геттер свойства role
     * @return role - роль пользователя (психолог)
     */
    public String getRole() { return role; }
    /**
     * Сеттер свойства role
     */
    public void setRole(String role) { this.role = role; }

    /**
     * Геттер свойства requests
     * @return requests - заявки спортсменов психологу
     */
    public Map<String, String> getRequests() { return requests; }
    /**
     * Сеттер свойства requests
     */
    public void setRequests(Map<String, String> requests) { this.requests = requests; }

    /**
     * Геттер свойства sportsmen
     * @return sportsmen - спортсмены психолога
     */
    public Map<String, String> getSportsmen() { return sportsmen; }
    /**
     * Сеттер свойства sportsmen
     */
    public void setSportsmen(Map<String, String> sportsmen) { this.sportsmen = sportsmen; }
}