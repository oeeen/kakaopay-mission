package dev.smjeon.kakaopay.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Year;
import java.util.List;

public interface FundRepository extends JpaRepository<Fund, Long> {
    @Query("SELECT f.institute, SUM(f.amount) FROM Fund f WHERE f.year=?1 GROUP BY f.institute")
    List<Object> findSumByYearGroupByInstitute(Year year);

    @Query("SELECT DISTINCT(f.year) FROM Fund f")
    List<Year> findDistinctYear();
}
