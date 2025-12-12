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

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("No hay suficiente stock para el producto: " + producto.getNombreProducto());
        }

        Optional<CarritoDetalle> detalleExistente = carritoUsuario.getDetalles()
                .stream()
                .filter(d -> d.getProductos().getIdProducto().equals(producto.getIdProducto()))
                .findFirst();

        if (detalleExistente.isPresent()) {
            detalleExistente.get().setCantidad(detalleExistente.get().getCantidad() + cantidad);
        } else {
            CarritoDetalle nuevoDetalle = new CarritoDetalle();
            nuevoDetalle.setIdDetalle(carritoUsuario.getDetalles().size() + 1); // ID simulado en memoria
            nuevoDetalle.setProductos(producto);
            nuevoDetalle.setCantidad(cantidad);
            carritoUsuario.getDetalles().add(nuevoDetalle);
        }

        // Restar stock del producto
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    // Actualizar cantidad de un detalle del carrito
    public void actualizarCantidad(Integer detalleId, int nuevaCantidad) {
        carritoUsuario.getDetalles().stream()
                .filter(d -> d.getIdDetalle().equals(detalleId))
                .findFirst()
                .ifPresent(d -> {
                    int diferencia = nuevaCantidad - d.getCantidad();
                    Productos producto = d.getProductos();
                    if (producto.getStock() < diferencia) {
                        throw new RuntimeException("No hay suficiente stock para el producto: " + producto.getNombreProducto());
                    }
                    d.setCantidad(nuevaCantidad);
                    producto.setStock(producto.getStock() - diferencia);
                    productoRepository.save(producto);
                });
    }

    // Eliminar detalle del carrito
    public void eliminarDetalle(Integer detalleId) {
        carritoUsuario.getDetalles().stream()
                .filter(d -> d.getIdDetalle().equals(detalleId))
                .findFirst()
                .ifPresent(d -> {
                    // Devolver stock al producto
                    Productos producto = d.getProductos();
                    producto.setStock(producto.getStock() + d.getCantidad());
                    productoRepository.save(producto);

                    carritoUsuario.getDetalles().remove(d);
                });
    }

    // Obtener producto desde BD
    private Productos obtenerProductoPorId(Long idProducto) {
        return productoRepository.findById(idProducto.intValue()).orElse(null);
    }
    
    public void vaciarCarrito() {
    carritoUsuario.getDetalles().clear();
}

}
