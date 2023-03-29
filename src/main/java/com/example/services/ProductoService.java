package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.entities.Producto;

public interface ProductoService {
    // El sort con import de domain
    public List<Producto> findAll(Sort sort);
    //Pageable de domain
    public Page<Producto> findAll(Pageable pageable);
    //le puedes pedir producto
    public Producto findById(long id);
    public Producto save (Producto producto);
    public void delete (Producto producto);
}
