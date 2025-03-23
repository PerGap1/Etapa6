package com.ogsbreakout.etapa7.repository;

import com.ogsbreakout.etapa7.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Integer> {
    List<Jogo> findByTituloContaining(String titulo);
    Jogo findByTitulo(String titulo);
    boolean existsByTitulo(String titulo);
}
