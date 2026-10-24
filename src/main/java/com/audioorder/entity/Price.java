package com.audioorder.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Прайс_товару")
public class Price {
    @Id
    @Column(name = "idПрайсу")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idВиробника")
    private Manufacturer manufacturer;

    @Column(name = "Найменування товару")
    private String productName;

    @Column(name = "Опис товару")
    private String description;

    @Column(name = "Ціна")
    private BigDecimal price;

    // Геттери і сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Manufacturer getManufacturer() { return manufacturer; }
    public void setManufacturer(Manufacturer manufacturer) { this.manufacturer = manufacturer; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
} 