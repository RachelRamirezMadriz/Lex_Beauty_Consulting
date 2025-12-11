package LexBeautyConsulting.demo.controller;

import LexBeautyConsulting.demo.domain.Carrito;
import LexBeautyConsulting.demo.services.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // Agregar producto al carrito desde JS
    @PostMapping("/agregar")
    @ResponseBody
    public String agregar(@RequestParam Long idProducto,
                          @RequestParam Integer cantidad) {
        carritoService.agregarProducto(idProducto, cantidad);
        return "OK";
    }

    // Mostrar carrito
    @GetMapping("/verCarrito")
    public String verCarrito(Model model) {
        Carrito carrito = carritoService.obtenerCarritoUsuario();
        model.addAttribute("carrito", carrito);
        return "carrito/verCarrito"; 
    }

    // Actualizar cantidad de un detalle del carrito
    @PostMapping("/actualizar")
    public String actualizarCantidad(@RequestParam("detalleId") Integer detalleId,
                                     @RequestParam("cantidad") int cantidad) {
        carritoService.actualizarCantidad(detalleId, cantidad);
        return "redirect:/carrito/verCarrito";
    }

    // Eliminar un detalle del carrito
    @PostMapping("/eliminar")
    public String eliminarDetalle(@RequestParam("detalleId") Integer detalleId) {
        carritoService.eliminarDetalle(detalleId);
        return "redirect:/carrito/verCarrito";
    }
}