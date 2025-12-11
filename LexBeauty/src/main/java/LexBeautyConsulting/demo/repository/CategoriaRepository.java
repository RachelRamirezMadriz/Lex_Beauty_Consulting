package LexBeautyConsulting.demo.repository;

import LexBeautyConsulting.demo.domain.Categorias;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface CategoriaRepository extends JpaRepository<Categorias,Integer>{
    public List<Categorias> findByActivoTrue();

    public List<Categorias> findByNombreCategoriaContainingIgnoreCase(String nombreCategoria);
 
}
