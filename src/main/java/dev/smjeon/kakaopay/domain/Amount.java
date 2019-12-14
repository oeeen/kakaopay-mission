package dev.smjeon.kakaopay.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Amount {
    private Integer amount;

    private Amount() {
    }

    public Amount(final String amount) {
        this.amount = Integer.parseInt(validate(amount));
    }

    public Integer getAmount() {
        return amount;
    }

    private String validate(String amount) {
        return amount.replaceAll(",", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount1 = (Amount) o;
        return Objects.equals(amount, amount1.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
