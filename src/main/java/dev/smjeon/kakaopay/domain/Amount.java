package dev.smjeon.kakaopay.domain;

import javax.persistence.Embeddable;

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
}
