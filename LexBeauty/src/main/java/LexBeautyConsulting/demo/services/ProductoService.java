package LexBeautyConsulting.demo.services;

import LexBeautyConsulting.demo.domain.Productos;
import LexBeautyConsulting.demo.repository.ProductoRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    private final FirebaseStorageService firebaseStorageService;
    
    //Constructor de la clase
    public ProductoService(ProductoRepository productoRepository, FirebaseStorageService firebaseStorageService) {
        this.productoRepository = productoRepository;
        this.firebaseStorageService = firebaseStorageService;
    }
    
    @Transactional(readOnly = true)
    public List<Productos> getProductos(boolean activo) {
        if(activo){
            return productoRepository.findByActivoTrue();
        }
        
        return productoRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Productos> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }
    
    @Transactional
    public void save(Productos productos, MultipartFile imagenFile) {
        productos = productoRepository.save(productos);
        if(!imagenFile.isEmpty()) {
            try{
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "producto",
                        productos.getIdProducto());
                productos.setRutaImagen(rutaImagen);
                productoRepository.save(productos);
            } catch (IOException e) {

            }
        }
    }
    
    //Verifica si el producto existe
    @Transactional
    public void delete(Integer idProducto){
        if(!productoRepository.existsById(idProducto)) {
            throw new IllegalArgumentException("El producto que contiene el ID" +idProducto+ " no existe en el sistema.");
        }
        try{
            productoRepository.deleteById(idProducto);
        }catch (DataIntegrityViolationException e){
            throw new IllegalStateException ("No es posible eliminar este producto ya que cuenta con datos asociados", e);
        }
    }
    
    @Transactional(readOnly = true)
    public List<Productos> buscarProductos(String nombre, BigDecimal precioMin, BigDecimal precioMax, boolean soloActivos) {
        boolean tieneNombre = nombre != null && !nombre.isBlank();
        boolean tienePrecio = precioMin != null || precioMax != null;

        List<Productos> productos;

        if (tieneNombre && tienePrecio) {
            BigDecimal min = precioMin != null ? precioMin : BigDecimal.ZERO;
            BigDecimal max = precioMax != null ? precioMax : BigDecimal.valueOf(999_999_999);
            if (max.compareTo(min) < 0) {
                BigDecimal tmp = min;
                min = max;
                max = tmp;
            }
            productos = productoRepository.findByNombreProductoContainingIgnoreCaseAndPrecioBetween(
                    nombre.trim(), min, max);
        } else if (tieneNombre) {
            productos = productoRepository.findByNombreProductoContainingIgnoreCase(nombre.trim());
        } else if (tienePrecio) {
            BigDecimal min = precioMin != null ? precioMin : BigDecimal.ZERO;
            BigDecimal max = precioMax != null ? precioMax : BigDecimal.valueOf(999_999_999);
            if (max.compareTo(min) < 0) {
                BigDecimal tmp = min;
                min = max;
                max = tmp;
            }
            productos = productoRepository.findByPrecioBetween(min, max);
        } else {
            productos = productoRepository.findAll();
        }

        if (soloActivos) {
            productos = productos.stream().filter(Productos::isActivo).toList();
        }

        return productos;
    }
}
