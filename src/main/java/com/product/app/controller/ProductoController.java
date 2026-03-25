package com.product.app.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.product.app.model.Producto;
import com.product.app.service.ProductoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    

    /**
     * Obtiene la lista de todos los productos.
     * 
     * @return Lista de productos.
     */
    @GetMapping()
    public List<Producto> listaProductos() {
        return productoService.listarTodo();
    }

    /**
     * Obtiene un producto por su ID.
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo producto.
     * 
     * @param producto
     * @return
     */
    @PostMapping()
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.crearProducto(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto productoActualizado) {

        return productoService.actualizarProducto(id, productoActualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un producto por su ID.
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        return productoService.eliminarProducto(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
