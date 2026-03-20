package com.product.app.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.product.app.model.Producto;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
}
