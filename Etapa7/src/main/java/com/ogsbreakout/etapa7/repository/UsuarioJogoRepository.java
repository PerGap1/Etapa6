package com.ogsbreakout.etapa7.repository;

import com.ogsbreakout.etapa7.model.UsuarioJogo;
import com.ogsbreakout.etapa7.model.Usuario;
import com.ogsbreakout.etapa7.model.Jogo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioJogoRepository extends JpaRepository<UsuarioJogo, Integer> {
    UsuarioJogo findByUsuarioAndJogo(Usuario usuario, Jogo jogo);
    List<UsuarioJogo> findByUsuario(Usuario usuario);
}