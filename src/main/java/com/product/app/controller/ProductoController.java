package com.product.app.controller;

import org.springframework.web.bind.annotation.RestController;

import com.product.app.model.Producto;
import com.product.app.repository.ProductoRepository;

import jakarta.websocket.server.PathParam;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

  

@RestController 
@RequestMapping("/api/productos")
public class ProductoController {
    
    // Inyección de dependencia del repositorio
    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping()
    public List<Producto> listaProductos() {
        return productoRepository.findAll();
    }

@GetMapping("/{id}")
public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
    return productoRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());


}

 @PostMapping()
 public Producto crearProducto(@RequestBody Producto producto) {
     return productoRepository.save(producto);
 }
 

}
