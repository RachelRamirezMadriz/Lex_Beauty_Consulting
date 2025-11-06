package LexBeautyConsulting.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name="Usuario")
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario")
    private Integer idUsuario;

    @Column(nullable=false, length=100, unique=true, name = "nombre")
    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(max = 100, message = "El nombre de usuario no puede tener más de 100 caracteres.")
    private String nombreUsuario;
//     PENDIENTE MOSTRAR MSG SI EL USERNAME YA EXISTE

    @Column(nullable=false, length=100, unique=true, name = "correo")
    @Email(message = "Correo electrónico inválido.")
    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    @Size(max = 100, message = "El correo electrónico no puede tener más de 100 caracteres.")
    private String email;

    @Column(nullable = false, name = "contrasena")
    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Size(min = 7, message = "La contraseña debe tener mínimo 7 caracteres.")
    @Size(max = 255, message = "La contraseña no puede tener más de 255 caracteres")
    private String password;

    @Column(length = 20)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_rol" , nullable = false)
    private Roles roles;
}
