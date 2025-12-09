package LexBeautyConsulting.demo.controller;

import LexBeautyConsulting.demo.domain.Usuarios;
import LexBeautyConsulting.demo.services.UsuarioService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/listado")
    public String inicio(Model model) {
        var usuarios = usuarioService.getUsuarios(false);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "/usuario/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Usuarios usuario,
            BindingResult bindingResult,
            @RequestParam("imagenFile") MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Hay errores en el formulario.");

            if (usuario.getIdUsuario() == null) {
                return "redirect:/usuario/listado";
            }

            return "redirect:/usuario/modificar/" + usuario.getIdUsuario();
        }

        usuarioService.save(usuario, imagenFile, true);

        redirectAttributes.addFlashAttribute("todoOk",
                "Usuario guardado correctamente.");

        return "redirect:/usuario/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idUsuario,
            RedirectAttributes redirectAttributes) {

        try {
            usuarioService.delete(idUsuario);
            redirectAttributes.addFlashAttribute("todoOk",
                    "Usuario eliminado correctamente.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error",
                    "El usuario no existe.");

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar el usuario porque tiene datos asociados.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ocurrió un error al eliminar el usuario.");
        }

        return "redirect:/usuario/listado";
    }

    @GetMapping("/modificar/{idUsuario}")
    public String modificar(@PathVariable("idUsuario") Integer idUsuario,
            Model model, RedirectAttributes redirectAttributes) {

        Optional<Usuarios> usuarioOpt = usuarioService.getUsuario(idUsuario);

        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    "El usuario no fue encontrado.");
            return "redirect:/usuario/listado";
        }

        Usuarios usuario = usuarioOpt.get();
        usuario.setPassword("");
        model.addAttribute("usuario", usuario);
        return "/usuario/modifica";
    }
}
