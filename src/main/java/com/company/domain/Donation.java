package com.company.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "donations")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=true)
public class Donation extends AbstractItem {

    @Column(name = "amount", nullable = false)
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    private Donor donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charity_id")
    private Charity charity;

    public Donation(Double amount) {
        this.setId(null);
        this.amount = amount;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Donation[date=" + getDateCreated());
        sb.append(", amount=" + amount);
        sb.append("]");

        return sb.toString();
    }
}
