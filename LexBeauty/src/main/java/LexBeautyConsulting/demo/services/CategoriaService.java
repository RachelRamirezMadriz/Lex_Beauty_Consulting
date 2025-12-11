package LexBeautyConsulting.demo.services;

import LexBeautyConsulting.demo.domain.Categorias;
import LexBeautyConsulting.demo.repository.CategoriaRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {
    
    /*Poder comunicarme con la BD que se definio en el Repository*/
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Transactional (readOnly=true) /*Realizar solo lectura a la BD*/
    public List<Categorias> getCategorias(boolean activo){
        if (activo){
            return categoriaRepository.findByActivoTrue(); //Si hay activos devuelve esos
        }
        
        return categoriaRepository.findAll();//sino, devuelve todos
    }
    
    //Obtener categoria por medio del id
    @Transactional(readOnly = true)
    public Optional<Categorias> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }
    
    //Comunicacion con el servicio de firebase
    @Autowired 
    private FirebaseStorageService firebaseStorageService;
    
    
    //Metodo para guardar la imagen en la BD de Firebase
    @Transactional
    public void save(Categorias categorias, MultipartFile imagenFile) {
        categorias = categoriaRepository.save(categorias);
        if(!imagenFile.isEmpty()){
            try{
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "categoria",
                        categorias.getIdCategoria());
                categorias.setRutaImagen(rutaImagen);
                categoriaRepository.save(categorias);
            } catch(IOException e) {
                        
            }
        }
    }
    
    
    //Verifica si la categoria existe
    @Transactional
    public void delete(Integer idCategoria){
        if(!categoriaRepository.existsById(idCategoria)) {
            throw new IllegalArgumentException("La categoria que contiene el ID" +idCategoria+ " no existe en el sistema.");
        }
        try{
            categoriaRepository.deleteById(idCategoria);
        }catch (DataIntegrityViolationException e){
            throw new IllegalStateException ("No es posible eliminar esta categoria ya que cuenta con datos asociados", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Categorias> buscarCategorias(String nombre, boolean soloActivas) {
        boolean tieneNombre = nombre != null && !nombre.isBlank();

        List<Categorias> categorias = tieneNombre
                ? categoriaRepository.findByNombreCategoriaContainingIgnoreCase(nombre.trim())
                : categoriaRepository.findAll();

        if (soloActivas) {
            categorias = categorias.stream().filter(Categorias::isActivo).toList();
        }

        return categorias;
    }
}

    
    

