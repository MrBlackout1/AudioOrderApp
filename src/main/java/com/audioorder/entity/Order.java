package com.audioorder.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Замовлення_аудіотехніки")
public class Order {
    @Id
    @Column(name = "idЧеку")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idМенеджера")
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "idКлієнта")
    private Client client;

    @Column(name = "Дата замовлення")
    private LocalDateTime orderDate;

    @Column(name = "Дата оплати")
    private LocalDateTime paymentDate;

    // Геттери і сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Manager getManager() { return manager; }
    public void setManager(Manager manager) { this.manager = manager; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
} 