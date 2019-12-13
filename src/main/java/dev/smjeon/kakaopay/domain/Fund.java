package dev.smjeon.kakaopay.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Month;
import java.time.Year;
import java.util.Objects;

@Entity
public class Fund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Year year;

    private Month month;

    @ManyToOne
    private Institute institute;

    @Embedded
    private Amount amount;

    public Fund(Year year, Month month, Institute institute, Amount amount) {
        this.year = year;
        this.month = month;
        this.institute = institute;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Year getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public Institute getInstitute() {
        return institute;
    }

    public Amount getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fund fund = (Fund) o;
        return amount == fund.amount &&
                Objects.equals(id, fund.id) &&
                Objects.equals(year, fund.year) &&
                month == fund.month &&
                Objects.equals(institute, fund.institute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, month, institute, amount);
    }
}