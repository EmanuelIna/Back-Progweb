package com.example.API_commerce_pw.repositories;
import com.example.API_commerce_pw.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario,String> {
 Optional<Usuario> findByEmail(String email);
 boolean existsByEmail(String email);
}
