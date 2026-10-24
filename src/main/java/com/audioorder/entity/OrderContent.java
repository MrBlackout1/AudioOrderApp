package com.audioorder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Вміст_замовлення")
public class OrderContent {
    @EmbeddedId
    private OrderContentId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "idЧеку")
    private Order order;

    @ManyToOne
    @MapsId("priceId")
    @JoinColumn(name = "idПрайсу")
    private Price price;

    @Column(name = "К-сть одиниць товару")
    private Integer quantity;

    // Геттери і сеттери
    public OrderContentId getId() { return id; }
    public void setId(OrderContentId id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Price getPrice() { return price; }
    public void setPrice(Price price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
} 