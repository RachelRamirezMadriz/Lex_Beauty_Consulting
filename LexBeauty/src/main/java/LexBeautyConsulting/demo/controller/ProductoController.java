package LexBeautyConsulting.demo.controller;

import LexBeautyConsulting.demo.domain.Productos;
import LexBeautyConsulting.demo.services.CategoriaService;
import LexBeautyConsulting.demo.services.ProductoService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping("/listado")
    public String listado(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            Model model) {
       var productos = productoService.buscarProductos(nombre, precioMin, precioMax, false);
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("nombre", nombre);
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "producto/listado";
    }

    @GetMapping("/disponibles")
    public String disponibles(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            Model model) {
        var productos = productoService.buscarProductos(nombre, precioMin, precioMax, true);
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("nombre", nombre);
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        return "producto/disponibles";
    }
    
    //Obtener los mensajes de las properties anteriormente definidas
    @Autowired
    private MessageSource messageSource;
    
    @PostMapping("/guardar")
    public String guardar(@Valid Productos productos, @RequestParam MultipartFile imagenFile, RedirectAttributes redirectAttributes) {

        productoService.save(productos, imagenFile);
        redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));

        return "redirect:/producto/listado";
    }
    
    @PostMapping("/eliminar")
    public String eliminar (@RequestParam Integer idProducto, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try{
            productoService.delete(idProducto);
        }catch(IllegalArgumentException e){
            titulo = "error";
            detalle = "producto.error01";
        }catch (IllegalStateException e) {
            titulo = "error";
            detalle = "producto.error02";
        }catch (Exception e) {
            titulo = "error";
            detalle = "producto.error03";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/producto/listado";
    }
    
    
    @GetMapping("/modificar/{idProducto}")  
    public String modificar(@PathVariable("idProducto") Integer idProducto, Model model, RedirectAttributes redirectAttributes) {
        Optional<Productos> productoOpt = productoService.getProducto(idProducto);
        if (productoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("producto.error01", null, Locale.getDefault()));
            return "redirect:/producto/listado";  
        }
        model.addAttribute("producto", productoOpt.get());
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/producto/modifica";
    }
}
