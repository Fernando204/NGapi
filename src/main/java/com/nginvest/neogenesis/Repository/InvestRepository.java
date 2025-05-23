package com.nginvest.neogenesis.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nginvest.neogenesis.Model.Investimento;

public interface InvestRepository extends JpaRepository<Investimento,Long> {
    List<Investimento> findByUserId(long userId);
    Optional<Investimento> findById(long id);
    boolean existsById(long id);
}
