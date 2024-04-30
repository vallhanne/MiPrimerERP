package org.example;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "InvoiceLine", schema = "Chinook", catalog = "")
public class InvoiceLineClass {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "InvoiceLineId", nullable = false)
    private int invoiceLineId;
    @Basic
    @Column(name = "InvoiceId", nullable = false)
    private int invoiceId;
    @Basic
    @Column(name = "TrackId", nullable = false)
    private int trackId;
    @Basic
    @Column(name = "UnitPrice", nullable = false, precision = 2)
    private BigDecimal unitPrice;
    @Basic
    @Column(name = "Quantity", nullable = false)
    private int quantity;

    public int getInvoiceLineId() {
        return invoiceLineId;
    }

    public void setInvoiceLineId(int invoiceLineId) {
        this.invoiceLineId = invoiceLineId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvoiceLineClass that = (InvoiceLineClass) o;

        if (invoiceLineId != that.invoiceLineId) return false;
        if (invoiceId != that.invoiceId) return false;
        if (trackId != that.trackId) return false;
        if (quantity != that.quantity) return false;
        if (unitPrice != null ? !unitPrice.equals(that.unitPrice) : that.unitPrice != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = invoiceLineId;
        result = 31 * result + invoiceId;
        result = 31 * result + trackId;
        result = 31 * result + (unitPrice != null ? unitPrice.hashCode() : 0);
        result = 31 * result + quantity;
        return result;
    }
}
