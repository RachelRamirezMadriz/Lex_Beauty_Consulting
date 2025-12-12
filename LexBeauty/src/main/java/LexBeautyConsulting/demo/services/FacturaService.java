/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LexBeautyConsulting.demo.services;

import LexBeautyConsulting.demo.domain.Carrito;
import LexBeautyConsulting.demo.domain.CarritoDetalle;
import LexBeautyConsulting.demo.domain.DetalleFactura;
import LexBeautyConsulting.demo.domain.Factura;
import LexBeautyConsulting.demo.domain.Productos;
import LexBeautyConsulting.demo.domain.Usuarios;
import LexBeautyConsulting.demo.repository.DetalleFacturaRepository;
import LexBeautyConsulting.demo.repository.FacturaRepository;
import LexBeautyConsulting.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;




/**
 *
 * @author alexa
 */
@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private DetalleFacturaRepository detalleFacturaRepository;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final BigDecimal IMPUESTO = new BigDecimal("0.13"); // 13%

    /***
     * Genera una factura a partir del carrito
     */
    public Factura generarFactura(Integer idUsuario) {

        // Obtener usuario
        Usuarios usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener carrito actual (en memoria)
        Carrito carrito = carritoService.obtenerCarritoUsuario();

        if (carrito.getDetalles().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Crear factura
        Factura factura = new Factura();
        factura.setUsuario(usuario);
        factura.setEstado(Factura.EstadoFactura.PENDIENTE);
        factura.setFecha(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalImpuestos = BigDecimal.ZERO;

        // Guardar factura base (para usar su ID)
        factura = facturaRepository.save(factura);

        // Crear cada detalle
        for (CarritoDetalle item : carrito.getDetalles()) {

            Productos producto = item.getProductos();

            BigDecimal precioConImpuesto = producto.getPrecio();
            BigDecimal divisor = BigDecimal.valueOf(1.13);

            BigDecimal precioSinImpuesto = precioConImpuesto.divide(divisor, 2, RoundingMode.HALF_UP);
            BigDecimal impuestoPorUnidad = precioConImpuesto.subtract(precioSinImpuesto);

            BigDecimal subtotal = precioSinImpuesto.multiply(BigDecimal.valueOf(item.getCantidad()));
            BigDecimal impuestos = impuestoPorUnidad.multiply(BigDecimal.valueOf(item.getCantidad()));

            total = total.add(precioConImpuesto.multiply(BigDecimal.valueOf(item.getCantidad())));
            totalImpuestos = totalImpuestos.add(impuestos);

            // Crear detalle
            DetalleFactura detalle = new DetalleFactura();
            detalle.setFactura(factura);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(precioConImpuesto);
            detalle.setSubtotal(subtotal);
            detalle.setImpuesto(impuestos);

            detalleFacturaRepository.save(detalle);
        }

        // Actualizar totales de factura
        factura.setTotal(total);
        factura.setTotalImpuestos(totalImpuestos);

        factura = facturaRepository.save(factura);

        // Vaciar carrito
        carritoService.vaciarCarrito();

        return factura;
    }

}

