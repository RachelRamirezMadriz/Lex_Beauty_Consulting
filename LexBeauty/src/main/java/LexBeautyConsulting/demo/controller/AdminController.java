package LexBeautyConsulting.demo.controller;

import LexBeautyConsulting.demo.domain.Usuarios;
import LexBeautyConsulting.demo.domain.Roles;
import LexBeautyConsulting.demo.services.UsuarioService;
import LexBeautyConsulting.demo.repository.RolRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuarios> usuarios = usuarioService.getUsuarios(false);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/listado";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuarios());
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/listado";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuarios usuario,
                                  BindingResult result,
                                  @RequestParam("idRol") Integer idRol,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolRepository.findAll());
            model.addAttribute("usuarios", usuarioService.getUsuarios(false));
            return "admin/listado";
        }

        try {
            Roles rol = rolRepository.findById(idRol).orElse(null);
            if (rol != null) {
                usuario.setRoles(rol);
            }
            usuarioService.saveConRol(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuarios> usuarioOpt = usuarioService.getUsuario(id);

        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/admin/usuarios";
        }

        Usuarios usuario = usuarioOpt.get();
        usuario.setPassword("");
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/modifica";
    }

    @PostMapping("/usuarios/actualizar")
    public String actualizarUsuario(@ModelAttribute("usuario") Usuarios usuario,
                                     @RequestParam("idRol") Integer idRol,
                                     @RequestParam(value = "password", required = false) String password,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            Roles rol = rolRepository.findById(idRol).orElse(null);
            if (rol != null) {
                usuario.setRoles(rol);
            }
            
            // Manejar password vacío
            Optional<Usuarios> existente = usuarioService.getUsuario(usuario.getIdUsuario());
            if (existente.isPresent()) {
                if (password == null || password.isEmpty()) {
                    usuario.setPassword(existente.get().getPassword());
                } else {
                    usuario.setPassword(password);
                }
            }
            
            usuarioService.actualizarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.delete(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar usuario");
        }
        return "redirect:/admin/usuarios";
    }
}
