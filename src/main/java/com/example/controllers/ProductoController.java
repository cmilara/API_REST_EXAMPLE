package com.example.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Producto;
import com.example.services.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

/** El metodo siguiente va a responder a una peticion (request) del tipo:
 * http://localhost:8080/productos?page=1&size=4
 * tiene que ser capaz de devolver un listado de productos paginados o no,
 * pero en cualquier caso ordenados por un criterio(nombre, descripcion, etc)
 * 
 * LA peticion anterior implica @RequestParam
 * /productos/3 es @PathVAriable
 * */

    @GetMapping
    public ResponseEntity<List<Producto>> findAll(@RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "size", required = false)Integer size){
        //para agregar a la respuestta info si ha ido bien o mal(ResponseEntity)

        ResponseEntity<List<Producto>> responseEntity = null;
        List<Producto> productos = new ArrayList<>();

        Sort sortByNombre = Sort.by("nombre");
        if(page != null && size != null){
            
            // con paginacion y ordenamiento
            try {
                Pageable pageable = PageRequest.of(page, size, sortByNombre);
                Page<Producto> productosPaginados = productoService.findAll(pageable);
                productos = productosPaginados.getContent();
                
                responseEntity = new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);

            } catch (Exception e) {
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }else{

            //sin paginacion pero con ordenamiento

            try {
                productos = productoService.findAll(sortByNombre);
                responseEntity = new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
            } catch (Exception e) {
                responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        }    

        return responseEntity;

    }
    /**Recupera producto por id
     * Va a responder a una peticion del tipo http://localhost:8080/productos/2
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable(name = "id")Integer id){

        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> responseAsMap =new HashMap<>();


            try {
                Producto producto = productoService.findById(id);
                if(producto != null){
                String successMessage = "Se ha encontrado el producto con id: " +id;
                responseAsMap.put("mensaje", successMessage);
                responseAsMap.put("producto", producto);
                responseEntity= new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);

                }else{
                String errorMessage ="No se ha encontrado el producto con id: " +id;
                responseAsMap.put("error", "errorMessage");
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.NOT_FOUND);

                }

                
            } catch (Exception e) {
                String errorGrave = "Error grave";
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        return responseEntity;

    }









    /**El método siguiente es de ejemplo par entender el formato JSON, no tiene que ver con el proyecto */

    // @GetMapping
    // public List<String> nombres(){
    //     List<String> nombres = Arrays.asList(
    //    "salma", "judith","ELisabeth"

    //     );
    //     return nombres;
    // }


}
