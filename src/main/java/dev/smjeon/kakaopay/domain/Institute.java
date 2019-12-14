package dev.smjeon.kakaopay.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Institute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
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

    public String getCode() {
        return code;
    }
}
