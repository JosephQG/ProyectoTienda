package com.tienda.service;

import com.tienda.domain.Categoria;
import com.tienda.repository.CategoriaRepository;
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
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    
    @Transactional(readOnly=true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) {
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }
    @Transactional(readOnly=true)
    public Optional<Categoria> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }
    @Transactional
    public void delete(Integer idCategoria) {
       // se valida que el idCategoria exista...
       if(!categoriaRepository.existsById(idCategoria)) {
           //se lanza una excepcion que no esta el idCategoria
           throw new IllegalArgumentException("La categoria"+idCategoria+" no existe...");
           
       }
       try {
           categoriaRepository.deleteById(idCategoria);
       } catch (DataIntegrityViolationException e) {
           throw new IllegalStateException("No se puede eliminar la categoria, tiene datos asociados"+e);
       }
    }
    
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    
    //este metodo tiene doble funcion.. si idCategoria esta vacio, se inserta el registro
    //si idCategoria, tiene informacion, se actualiza el registro
    @Transactional
    public void save (Categoria categoria, MultipartFile imagenFile) {
        categoria = categoriaRepository.save(categoria);
        if (!imagenFile.isEmpty()) {
            try {
                String rutaImagen = firebaseStorageService.uploadImage(imagenFile,"categoria", categoria.getIdCategoria());
                categoria.setRutaImagen(rutaImagen);
                categoriaRepository.save(categoria);
            } catch (IOException e) {
                
            }
        }
    }
    
            
}