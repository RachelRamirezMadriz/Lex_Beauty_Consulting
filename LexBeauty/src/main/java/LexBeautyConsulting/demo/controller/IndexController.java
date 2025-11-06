package LexBeautyConsulting.demo.controller;

import LexBeautyConsulting.demo.services.CategoriaService;
import LexBeautyConsulting.demo.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class IndexController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        var categorias = categoriaService.getCategorias(true);
        var productos = productoService.getProductos(true);

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        // CARGAR CATEGORIAS Y PRODUCTOS EN EL INDEX
        return "index";
    }

    @GetMapping("/nosotros")
    public String nosotros() {
        return "nosotros/listado";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto/listado";
    }
}
