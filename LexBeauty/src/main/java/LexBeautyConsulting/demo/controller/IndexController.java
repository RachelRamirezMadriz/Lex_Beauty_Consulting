package LexBeautyConsulting.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("usuario", authentication.getName());
            
            // Obtener el rol del usuario
            String rol = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("");
            
            model.addAttribute("rol", rol);
        }
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }
}
