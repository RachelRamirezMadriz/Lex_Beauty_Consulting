package LexBeautyConsulting.demo.controller;

import LexBeautyConsulting.demo.domain.Carrito;
import LexBeautyConsulting.demo.domain.Pedido;
import LexBeautyConsulting.demo.domain.Usuarios;
import LexBeautyConsulting.demo.services.CarritoService;
import LexBeautyConsulting.demo.services.PedidoService;
import LexBeautyConsulting.demo.services.UsuarioService;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private UsuarioService usuarioService;

    // Agregar producto al carrito desde JS
    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<String> agregar(@RequestParam Long idProducto,
                          @RequestParam Integer cantidad) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("LOGIN_REQUIRED");
        }
        carritoService.agregarProducto(idProducto, cantidad);
        return ResponseEntity.ok("OK");
    }

    // Mostrar carrito
    @GetMapping("/verCarrito")
    public String verCarrito(Model model) {
        Carrito carrito = carritoService.obtenerCarritoUsuario();

        if (carrito == null) {
            carrito = new Carrito();
            carrito.setDetalles(new ArrayList<>());
        }

        // Calcula total seguro con BigDecimal
        BigDecimal totalCarrito = carrito.getDetalles().stream()
                .map(d -> d.getProductos().getPrecio().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("carrito", carrito);
        model.addAttribute("totalCarrito", totalCarrito);

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

    @PostMapping("/finalizar")
    public String finalizarCompra(Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        Carrito carrito = carritoService.obtenerCarritoUsuario();
        if (carrito == null || carrito.getDetalles() == null || carrito.getDetalles().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El carrito está vacío.");
            return "redirect:/carrito/verCarrito";
        }

        BigDecimal totalCarrito = carrito.getDetalles().stream()
                .map(d -> d.getProductos().getPrecio().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Usuarios usuario = usuarioService.findByEmail(authentication.getName())
                .orElse(null);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "No se pudo obtener la información del usuario.");
            return "redirect:/carrito/verCarrito";
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setTotal(totalCarrito);
        pedido = pedidoService.guardar(pedido);

        var detallesFactura = new ArrayList<>(carrito.getDetalles());
        carritoService.vaciarCarrito();

        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detallesFactura);
        model.addAttribute("totalCarrito", totalCarrito);
        model.addAttribute("usuario", usuario);

        return "carrito/factura";
    }
}
