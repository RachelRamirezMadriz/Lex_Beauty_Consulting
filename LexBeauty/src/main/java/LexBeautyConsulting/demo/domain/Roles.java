package LexBeautyConsulting.demo.domain;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name="Rol")
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_rol")
    private Integer idRol;

    @Column(name = "nombre_rol", nullable = false, unique = true, length = 50)
    @NotBlank(message = "El rol no puede estar vacío.")
    private String nombreRol; // 2 roles ROLE_ADMIN y ROLE_CLIENTE

    @OneToMany(mappedBy = "roles")
    private List<Usuarios> usuarios;
}
