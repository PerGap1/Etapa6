package com.ogsbreakout.etapa7.repository;

import com.ogsbreakout.etapa7.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByNomeContaining(String nome);
    Usuario findByNomeAndSenha(String nome, String senha);
    Usuario findByNome(String nome);
    boolean existsByNome(String nome);
    boolean existsByEmail(String email);
    boolean existsByApelido(String apelido);
    boolean existsBySenha(String senha);
}
