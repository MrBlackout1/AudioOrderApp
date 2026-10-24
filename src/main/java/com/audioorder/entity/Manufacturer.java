package com.audioorder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Довідник_виробників")
public class Manufacturer {
    @Id
    @Column(name = "idВиробника")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idКраїни")
    private Country country;

    @Column(name = "Назва виробника")
    private String name;

    // Геттери і сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
} 