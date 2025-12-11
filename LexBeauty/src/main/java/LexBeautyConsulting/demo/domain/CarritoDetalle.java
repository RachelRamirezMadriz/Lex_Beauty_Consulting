package LexBeautyConsulting.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detalle_carrito")
@Data
public class CarritoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Productos productos;

    @Column(name = "cantidad")
    private Integer cantidad;
}