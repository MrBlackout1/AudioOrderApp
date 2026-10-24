package com.audioorder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Довідник_клієнтів")
public class Client {
    @Id
    @Column(name = "idКлієнта")
    private Long id;

    @Column(name = "Найменування клієнта")
    private String name;

    @Column(name = "Телефон")
    private String phone;

    @Column(name = "E-mail")
    private String email;

    @Column(name = "Адреса")
    private String address;

    @Column(name = "ЄДРПОУ")
    private Integer edrpou;

    @Column(name = "МФО")
    private Integer mfo;

    @Column(name = "P/p")
    private String pp;

    // Геттери і сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getEdrpou() { return edrpou; }
    public void setEdrpou(Integer edrpou) { this.edrpou = edrpou; }
    public Integer getMfo() { return mfo; }
    public void setMfo(Integer mfo) { this.mfo = mfo; }
    public String getPp() { return pp; }
    public void setPp(String pp) { this.pp = pp; }
} 