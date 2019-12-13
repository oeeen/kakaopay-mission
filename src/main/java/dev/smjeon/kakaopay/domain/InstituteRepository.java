package dev.smjeon.kakaopay.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstituteRepository extends JpaRepository<Institute, Long> {
    Optional<Institute> findByName(String name);
}
