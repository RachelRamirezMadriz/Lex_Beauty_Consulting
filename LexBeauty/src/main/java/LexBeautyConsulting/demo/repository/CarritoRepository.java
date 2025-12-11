package LexBeautyConsulting.demo.repository;

import LexBeautyConsulting.demo.domain.Carrito;
import LexBeautyConsulting.demo.domain.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuario(Usuarios usuarios);
}
