/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package LexBeautyConsulting.demo.repository;

/**
 *
 * @author alexa
 */
import LexBeautyConsulting.demo.domain.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {

    // Obtener la factura activa (estado Pendiente) del usuario
    Factura findByUsuarioIdUsuarioAndEstado(Integer idUsuario, Factura.EstadoFactura estado);
}