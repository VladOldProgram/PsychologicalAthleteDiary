package ru.pad.objects;

/**
 * Класс объекта спортсмена со свойствами:
 * <b>name</b>, <b>surname</b>, <b>birthDate</b>, <b>email</b>, <b>password</b>, <b>role</b>,
 * <b>psychologist</b>, <b>requestAccepted</b>
 */
public class Sportsman {
    private String name, surname, birthDate, email, password, role, psychologist;

    private boolean requestAccepted;

    /**
     * Конструктор - создание нового объекта с неинициализированными свойствами
     */
    public Sportsman() {}

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param name имя спортсмена
     * @param surname фамилия спортсмена
     * @param birthDate дата рождения спортсмена
     * @param email email спортсмена
     * @param password пароль спортсмена
     * @param role роль пользователя (спортсмен)
     * @param psychologist психолог спортсмена
     * @param requestAccepted статус заявки (принята/еще не принята)
     */
    public Sportsman(
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

    /**
     * Геттер свойства name
     * @return name - имя спортсмена
     */
    public String getName() { return name; }
    /**
     * Сеттер свойства name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Геттер свойства surname
     * @return surname - фамилия спортсмена
     */
    public String getSurname() { return surname; }
    /**
     * Сеттер свойства surname
     */
    public void setSurname(String surname) { this.surname = surname; }

    /**
     * Геттер свойства birthDate
     * @return birthDate - дата рождения спортсмена
     */
    public String getBirthDate() { return birthDate; }
    /**
     * Сеттер свойства birthDate
     */
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    /**
     * Геттер свойства email
     * @return email - email спортсмена
     */
    public String getEmail() { return email; }
    /**
     * Сеттер свойства email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Геттер свойства password
     * @return password - пароль спортсмена
     */
    public String getPassword() { return password; }
    /**
     * Сеттер свойства password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Геттер свойства role
     * @return role - роль пользователя (спортсмен)
     */
    public String getRole() { return role; }
    /**
     * Сеттер свойства role
     */
    public void setRole(String role) { this.role = role; }

    /**
     * Геттер свойства psychologist
     * @return psychologist - психолог спортсмена
     */
    public String getPsychologist() { return psychologist; }
    /**
     * Сеттер свойства psychologist
     */
    public void setPsychologist(String psychologist) { this.psychologist = psychologist; }

    /**
     * Геттер свойства requestAccepted
     * @return requestAccepted - статус заявки (принята/еще не принята)
     */
    public boolean getRequestAccepted() { return requestAccepted; }
    /**
     * Сеттер свойства requestAccepted
     */
    public void setRequestAccepted(boolean requestAccepted) { this.requestAccepted = requestAccepted; }
}