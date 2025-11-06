package LexBeautyConsulting.demo.repository;

import LexBeautyConsulting.demo.domain.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RolRepository extends JpaRepository<Roles, Integer> {
    public Roles findByNombreRol(String nombreRol); // Debe devolver solamente 1 ROL
}
