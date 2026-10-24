package com.audioorder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Довідник_країн")
public class Country {
    @Id
    @Column(name = "idКраїни")
    private Long id;

    @Column(name = "Назва країни")
    private String name;

    // Геттери і сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
} 