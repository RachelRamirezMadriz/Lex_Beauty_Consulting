package LexBeautyConsulting.demo.service;

import LexBeautyConsulting.demo.domain.Pedido;
import LexBeautyConsulting.demo.repository.PedidoRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public Pedido getPedidoConUsuario(Integer idPedido) {

        return pedidoRepository.findByIdPedidoConUsuario(idPedido)
                .orElseThrow(() -> new NoSuchElementException("Pedido con ID " + idPedido + " no encontrado."));
    }

    @Transactional
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public Pedido getById(Integer idPedido) {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new NoSuchElementException("Pedido con ID " + idPedido + " no encontrado."));
    }

    @Transactional(readOnly = true)
    public Iterable<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public void eliminar(Integer idPedido) {
        if (!pedidoRepository.existsById(idPedido)) {
            throw new NoSuchElementException("Pedido con ID " + idPedido + " no existe.");
        }
        pedidoRepository.deleteById(idPedido);
    }
}
