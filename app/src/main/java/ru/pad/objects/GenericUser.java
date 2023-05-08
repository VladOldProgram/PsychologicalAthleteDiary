package ru.pad.objects;

/**
 * Класс объекта пользователя с общими свойствами пользователя-спортсмена и пользователя-психолога:
 * <b>name</b>, <b>surname</b>, <b>birthDate</b>, <b>email</b>, <b>password</b>, <b>role</b>
 */
public class GenericUser {
    private String name, surname, birthDate, email, password, role;

    /**
     * Конструктор - создание нового объекта с неинициализированными свойствами
     */
    public GenericUser() {}

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param name имя пользователя
     * @param surname фамилия пользователя
     * @param birthDate дата рождения пользователя
     * @param email email пользователя
     * @param password пароль пользователя
     * @param role роль пользователя (спортсмен/психолог)
     */
    public GenericUser(
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

    /**
     * Геттер свойства name
     * @return name - имя пользователя
     */
    public String getName() { return name; }
    /**
     * Сеттер свойства name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Геттер свойства surname
     * @return surname - фамилия пользователя
     */
    public String getSurname() { return surname; }
    /**
     * Сеттер свойства surname
     */
    public void setSurname(String surname) { this.surname = surname; }

    /**
     * Геттер свойства birthDate
     * @return birthDate - дата рождения пользователя
     */
    public String getBirthDate() { return birthDate; }
    /**
     * Сеттер свойства birthDate
     */
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    /**
     * Геттер свойства email
     * @return email - email пользователя
     */
    public String getEmail() { return email; }
    /**
     * Сеттер свойства email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Геттер свойства password
     * @return password - пароль пользователя
     */
    public String getPassword() { return password; }
    /**
     * Сеттер свойства password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Геттер свойства role
     * @return role - роль пользователя (спортсмен/психолог)
     */
    public String getRole() { return role; }
    /**
     * Сеттер свойства role
     */
    public void setRole(String role) { this.role = role; }
}