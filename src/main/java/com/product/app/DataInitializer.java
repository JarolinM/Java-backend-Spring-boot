package com.product.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.product.app.model.Producto;
import com.product.app.repository.ProductoRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(ProductoRepository repository) {
        return args -> {
            repository.save(new Producto("Laptop", "Computadora portatil 15 pulgadas", 999.99));
            repository.save(new Producto("Mouse", "Mouse inalambrico", 25.50));
            repository.save(new Producto("Teclado", "Teclado mecanico RGB", 75.00));
            repository.save(new Producto("Monitor", "Monitor 24 pulgadas Full HD", 150.00));
            repository.save(new Producto("Auriculares", "Auriculares con microfono", 45.99));
        };
    }
}

