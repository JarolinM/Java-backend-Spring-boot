package com.product.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.app.model.Producto;
import com.product.app.repository.ProductoRepository;

@Service
public class ProductoService {
    
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
 
    /**
     * Obtiene la lista de todos los productos.
     * @return Lista de productos.      
     */ 
    public List<Producto> listarTodo() {
        return productoRepository.findAll();
    }

    /**
     * Obtiene un producto por su ID.
     * @param id
     * @return
     */
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Crea un nuevo producto.
     * @param producto
     * @return
     */
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    } 

    /**
     * Actualiza un producto existente.
     * @param id
     * @param productoActualizado
     * @return
     */
    public Optional<Producto> actualizarProducto(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    return productoRepository.save(producto);
                });
    }

    /**
     * Elimina un producto por su ID.
     * @param id
     * @return
     */
    public boolean eliminarProducto(Long id){
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
