package LexBeautyConsulting.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "Producto")
public class Productos implements Serializable {

    private static final long serialVersionUID = 1L;

    // Clave primaria (el id_producto)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    // Nombre del producto
    @Column(name = "nombre_producto", nullable = false, length = 150)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres.")
    private String nombreProducto;

    // Descripción del producto
    @Column(nullable = false, length = 150)
    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 150, message = "La descripción no puede superar los 150 caracteres.")
    private String descripcion;

    // Precio del producto
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0.")
    private BigDecimal precio;

    // Cantidad en stock
    @Column(nullable = false)
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private Integer stock = 0;

    // Fecha de creación
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Fecha de última modificación
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion = LocalDateTime.now();
    
    //Ruta de la Imagen
    @Column(name = "ruta_imagen", length = 1024)
    private String rutaImagen;
    private boolean activo;

    // Relación con categoría (muchos productos -> una categoría)
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categorias categoria;
}