package LexBeautyConsulting.demo.repository;

import LexBeautyConsulting.demo.domain.Pedido;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // Cargar un Pedido junto con su Usuario
    @Query("SELECT p FROM Pedido p " +
           "LEFT JOIN FETCH p.usuario u " +
           "WHERE p.idPedido = :idPedido")
    Optional<Pedido> findByIdPedidoConUsuario(@Param("idPedido") Integer idPedido);
}
