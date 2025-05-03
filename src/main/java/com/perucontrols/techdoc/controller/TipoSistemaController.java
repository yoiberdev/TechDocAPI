package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.model.TipoSistema;
import com.perucontrols.techdoc.repository.TipoSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tipos-sistema")
public class TipoSistemaController {

    @Autowired
    private TipoSistemaRepository tipoSistemaRepository;

    // Obtener todos los tipos de sistema
    @GetMapping
    public ResponseEntity<List<TipoSistema>> getAllTiposSistema() {
        List<TipoSistema> tiposSistema = tipoSistemaRepository.findAll();
        return new ResponseEntity<>(tiposSistema, HttpStatus.OK);
    }

    // Obtener un tipo de sistema por ID
    @GetMapping("/{id}")
    public ResponseEntity<TipoSistema> getTipoSistemaById(@PathVariable Long id) {
        Optional<TipoSistema> tipoSistema = tipoSistemaRepository.findById(id);
        return tipoSistema.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Crear un nuevo tipo de sistema
    @PostMapping
    public ResponseEntity<TipoSistema> createTipoSistema(@RequestBody TipoSistema tipoSistema) {
        try {
            TipoSistema newTipoSistema = tipoSistemaRepository.save(tipoSistema);
            return new ResponseEntity<>(newTipoSistema, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un tipo de sistema existente
    @PutMapping("/{id}")
    public ResponseEntity<TipoSistema> updateTipoSistema(@PathVariable Long id, @RequestBody TipoSistema tipoSistema) {
        Optional<TipoSistema> tipoSistemaData = tipoSistemaRepository.findById(id);

        if (tipoSistemaData.isPresent()) {
            TipoSistema updatedTipoSistema = tipoSistemaData.get();
            updatedTipoSistema.setNombre(tipoSistema.getNombre());
            updatedTipoSistema.setDescripcion(tipoSistema.getDescripcion());
            updatedTipoSistema.setCategoria(tipoSistema.getCategoria());
            updatedTipoSistema.setFabricanteRecomendado(tipoSistema.getFabricanteRecomendado());
            updatedTipoSistema.setVidaUtilEstimada(tipoSistema.getVidaUtilEstimada());

            return new ResponseEntity<>(tipoSistemaRepository.save(updatedTipoSistema), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un tipo de sistema
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTipoSistema(@PathVariable Long id) {
        try {
            tipoSistemaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Buscar tipos de sistema por categoría
    @GetMapping("/buscar/categoria/{categoria}")
    public ResponseEntity<List<TipoSistema>> getTiposSistemaByCategoria(@PathVariable TipoSistema.CategoriaSistema categoria) {
        List<TipoSistema> tiposSistema = tipoSistemaRepository.findByCategoria(categoria);
        return new ResponseEntity<>(tiposSistema, HttpStatus.OK);
    }

    // Buscar tipos de sistema por nombre
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<TipoSistema>> getTiposSistemaByNombre(@PathVariable String nombre) {
        List<TipoSistema> tiposSistema = tipoSistemaRepository.findByNombreContaining(nombre);
        return new ResponseEntity<>(tiposSistema, HttpStatus.OK);
    }
}