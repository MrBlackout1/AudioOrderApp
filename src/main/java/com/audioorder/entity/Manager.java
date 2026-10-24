package com.audioorder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Довідник_менеджерів")
public class Manager {
    @Id
    @Column(name = "idМенеджера")
    private Long id;

    @Column(name = "Прізвище менеджера")
    private String lastName;

    @Column(name = "Ім'я менеджера")
    private String firstName;

    @Column(name = "По-батькові менеджера")
    private String middleName;

    @Column(name = "Телефон")
    private String phone;

    // Геттери і сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
} 