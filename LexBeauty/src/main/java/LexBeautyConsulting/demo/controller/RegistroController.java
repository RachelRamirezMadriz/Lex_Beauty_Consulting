package LexBeautyConsulting.demo.controller;

import LexBeautyConsulting.demo.domain.Usuarios;
import LexBeautyConsulting.demo.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistroController {

    @Autowired 
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuarios());
        return "registro/listado";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") Usuarios usuario,
                                   BindingResult resultado,
                                   Model model) {

        if (resultado.hasErrors()) {
            return "registro/listado";
        }

        if (usuarioService.findByEmail(usuario.getEmail()) != null) {
            resultado.rejectValue("email", "email.existencia", "Este correo ya existe");
            return "registro/listado";
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioService.saveClientes(usuario);

        return "redirect:/login";
    }
}
