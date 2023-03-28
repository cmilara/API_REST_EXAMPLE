package com.example.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entities.Producto;

public interface ProductoDao extends JpaRepository <Producto, Long> {

    /**
     * Vamos a necesitar tres métodos adicionales a los que genera el crud repository
     * (interface), para 
     * 1.Recuperar lista de productos ordenados
     * 2.Recuperar litado de productos paginados, que no traiga todos los productos sino de 10 en 10 o en 20 etc.
     * 3. Consulta para recuperar los productos o las presentaciones con sus productos correspondientes
     * sin tener que realizar una subconsulta que sería menos eficiente que un join a las entidades 
     * utilizando HQL (Hibernate Query Language)
     */
     
     /*
     * Crearemos unas consultas personalizadas para cuando se busque un productoo,
     * se recupere la presentacion conjuntamente con dicho producto, y tambien para
     * recuperar no todos los productos, sino por pagina, es decir, de 10 en 10, de 20
     * en 20, etc.
     * 
     * RECORDEMOS QUE: Cuando hemos creado las relaciones hemos especificado que 
     * la busqueda sea LAZY, para que no se traiga la presentacion siempre que se 
     * busque un producto, porque serian dos consultas, o una consulta con una 
     * subconsulta, que es menos eficiente que lo que vamos a hacer, hacer una sola 
     * consulta relacionando las entidades, y digo las entidades, porque aunque 
     * de la impresión que es una consulta de SQL no consultamos a las tablas de 
     * la base de datos sino a las entidades 
     * (esto se llama HQL (Hibernate Query Language))
     * 
     * Ademas, tambien podremos recuperar el listado de productos de forma ordenada, 
     * por algun criterio de ordenación, como por ejemplo por el nombre del producto, 
     * por la descripcion, etc.
     */


    //Elegir el sort de domain
    @Query(value = "select p from Producto p letf fetch p.presentacion")// esto es HQL
     public List <Producto>findAll(Sort sort);

     /**
      * El siguiente método recupera una página de producto
      */
      // page de domain y pageable tb de domain
      @Query(value = "select p from Producto p letf fetch p.presentacion", 
      countQuery = "select count(p) from Producto p left join p.presentacion")
      public Page<Producto> findAll(Pageable pageable);

    /**
      * El siguiente método recupera un producto por el id
      */

      @Query(value = "select p from Producto p letf fetch p.presentacion where p.id = :id")
        public List<Producto> findById(long id);


}
