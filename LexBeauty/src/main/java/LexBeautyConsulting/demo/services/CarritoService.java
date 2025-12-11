package LexBeautyConsulting.demo.services;

import LexBeautyConsulting.demo.domain.Carrito;
import LexBeautyConsulting.demo.domain.CarritoDetalle;
import LexBeautyConsulting.demo.domain.Productos;
import LexBeautyConsulting.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CarritoService {

    private Carrito carritoUsuario;

    @Autowired
    private ProductoRepository productoRepository;

    public CarritoService() {
        carritoUsuario = new Carrito();
        carritoUsuario.setDetalles(new ArrayList<>());
    }

    // Obtener carrito del usuario
    public Carrito obtenerCarritoUsuario() {
        return carritoUsuario;
    }

    // Agregar producto al carrito
    public void agregarProducto(Long idProducto, Integer cantidad) {
        Productos producto = obtenerProductoPorId(idProducto);
        if (producto == null) return;

        // Verificar si el producto ya está en el carrito
        Optional<CarritoDetalle> detalleExistente = carritoUsuario.getDetalles()
                .stream()
                .filter(d -> d.getProductos().getIdProducto().equals(producto.getIdProducto()))
                .findFirst();

        if (detalleExistente.isPresent()) {
            // Si ya existe, sumar la cantidad
            detalleExistente.get().setCantidad(detalleExistente.get().getCantidad() + cantidad);
        } else {
            // Si no existe, crear un nuevo detalle
            CarritoDetalle nuevoDetalle = new CarritoDetalle();
            nuevoDetalle.setIdDetalle(carritoUsuario.getDetalles().size() + 1); // ID simulado
            nuevoDetalle.setProductos(producto);
            nuevoDetalle.setCantidad(cantidad);
            carritoUsuario.getDetalles().add(nuevoDetalle);
        }
    }

    // Actualizar cantidad de un detalle del carrito
    public void actualizarCantidad(Integer detalleId, int cantidad) {
        carritoUsuario.getDetalles().stream()
                .filter(d -> d.getIdDetalle().equals(detalleId))
                .findFirst()
                .ifPresent(d -> d.setCantidad(cantidad));
    }

    // Eliminar detalle del carrito
    public void eliminarDetalle(Integer detalleId) {
        carritoUsuario.getDetalles().removeIf(d -> d.getIdDetalle().equals(detalleId));
    }

    // Método privado para obtener un producto por su ID desde la base de datos
    private Productos obtenerProductoPorId(Long idProducto) {
        return productoRepository.findById(idProducto.intValue()).orElse(null);
    }
}
