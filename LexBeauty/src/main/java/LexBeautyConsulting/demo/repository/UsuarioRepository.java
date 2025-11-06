package LexBeautyConsulting.demo.repository;

import LexBeautyConsulting.demo.domain.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuarios, Integer> {
    @Nullable
    public Optional<Usuarios> findByEmail(String email); // Optional maneja valores nulos, devuelve objeto Usuario si existe

    public boolean existsByEmail(String email);
}
