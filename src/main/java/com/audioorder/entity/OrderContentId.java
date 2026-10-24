package com.audioorder.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderContentId implements Serializable {
    @Column(name = "idЧеку")
    private Long orderId;

    @Column(name = "idПрайсу")
    private Long priceId;

    public OrderContentId() {}
    public OrderContentId(Long orderId, Long priceId) {
        this.orderId = orderId;
        this.priceId = priceId;
    }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getPriceId() { return priceId; }
    public void setPriceId(Long priceId) { this.priceId = priceId; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderContentId that = (OrderContentId) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(priceId, that.priceId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(orderId, priceId);
    }
} 