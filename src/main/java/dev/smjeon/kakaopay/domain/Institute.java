package dev.smjeon.kakaopay.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Institute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    private Institute() {
    }

    public Institute(String name) {
        this.name = name;
        this.code = InstituteCode.getInstituteCodeByName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
