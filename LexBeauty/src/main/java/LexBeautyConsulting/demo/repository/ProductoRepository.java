package LexBeautyConsulting.demo.repository;

import LexBeautyConsulting.demo.domain.Productos;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Productos, Integer> {

    public List<Productos> findByActivoTrue();

    // Consultas
    public List<Productos> findByNombreProductoContainingIgnoreCase(String nombreProducto);

    public List<Productos> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);

    public List<Productos> findByNombreProductoContainingIgnoreCaseAndPrecioBetween(
            String nombreProducto, BigDecimal precioMin, BigDecimal precioMax);
}
