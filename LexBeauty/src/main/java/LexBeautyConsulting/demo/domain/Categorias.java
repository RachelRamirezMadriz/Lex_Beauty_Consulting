package LexBeautyConsulting.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "Categoria")
public class Categorias implements Serializable {

    private static final long serialVersionUID = 1L;

    // Clave primaria (id_categoria)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    // Nombre de la categoría
    @Column(name = "nombre_categoria", nullable = false, length = 100)
    @NotBlank(message = "El nombre de la categoría no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres.")
    private String nombreCategoria;

    // Descripción (opcional)
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Fechas
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion = LocalDateTime.now();
    
    //Manejar ruta de la categoria
    @Column(length=1024)
    @Size(max=1024)
    private String rutaImagen;
    
    private boolean activo;

    // Relación uno a muchos: una categoría tiene muchos productos
    @OneToMany(mappedBy = "categoria")
    private List<Productos> productos;

    // Métodos automáticos para asignar fechas con el LocalDateTime
    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
}