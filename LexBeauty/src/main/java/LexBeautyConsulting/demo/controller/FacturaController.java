/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LexBeautyConsulting.demo.controller;

/**
 *
 * @author alexa
 */
import LexBeautyConsulting.demo.domain.Factura;
import LexBeautyConsulting.demo.domain.Usuarios;
import LexBeautyConsulting.demo.repository.UsuarioRepository;
import LexBeautyConsulting.demo.services.FacturaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Finalizar compra -> generar factura
     */
    @GetMapping("/finalizarCompra")
    public String finalizarCompra(Model model) {

        // TODO: Obtener usuario logueado
        // Esto es temporal mientras no tienes login real
        Usuarios usuario = usuarioRepository.findById(1).orElse(null);

        if (usuario == null) {
            throw new RuntimeException("No se pudo obtener el usuario actual.");
        }

        // Generar factura
        Factura factura = facturaService.generarFactura(usuario.getIdUsuario());

        // Enviar factura a la vista
        model.addAttribute("factura", factura);

        return "factura/factura"; // carpeta /templates/factura/factura.html
    }
}