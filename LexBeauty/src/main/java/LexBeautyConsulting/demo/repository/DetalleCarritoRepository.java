package LexBeautyConsulting.demo.repository;

import LexBeautyConsulting.demo.domain.Carrito;
import LexBeautyConsulting.demo.domain.CarritoDetalle;
import LexBeautyConsulting.demo.domain.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetalleCarritoRepository extends JpaRepository<CarritoDetalle, Long> {

    Optional<CarritoDetalle> findByCarritoAndProductos(Carrito carrito, Productos productos);
}
