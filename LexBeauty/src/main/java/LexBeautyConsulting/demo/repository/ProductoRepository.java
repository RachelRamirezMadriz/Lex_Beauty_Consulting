package LexBeautyConsulting.demo.repository;

import LexBeautyConsulting.demo.domain.Productos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Productos, Integer> {

    public List<Productos> findByActivoTrue();
}