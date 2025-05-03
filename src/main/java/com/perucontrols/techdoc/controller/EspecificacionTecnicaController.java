package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.model.EspecificacionTecnica;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.repository.EspecificacionTecnicaRepository;
import com.perucontrols.techdoc.repository.SistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/especificaciones-tecnicas")
public class EspecificacionTecnicaController {

    @Autowired
    private EspecificacionTecnicaRepository especificacionTecnicaRepository;

    @Autowired
    private SistemaRepository sistemaRepository;

    // Obtener todas las especificaciones técnicas
    @GetMapping
    public ResponseEntity<List<EspecificacionTecnica>> getAllEspecificacionesTecnicas() {
        List<EspecificacionTecnica> especificacionesTecnicas = especificacionTecnicaRepository.findAll();
        return new ResponseEntity<>(especificacionesTecnicas, HttpStatus.OK);
    }

    // Obtener una especificación técnica por ID
    @GetMapping("/{id}")
    public ResponseEntity<EspecificacionTecnica> getEspecificacionTecnicaById(@PathVariable Long id) {
        Optional<EspecificacionTecnica> especificacionTecnica = especificacionTecnicaRepository.findById(id);
        return especificacionTecnica.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Crear una nueva especificación técnica
    @PostMapping
    public ResponseEntity<EspecificacionTecnica> createEspecificacionTecnica(@RequestBody EspecificacionTecnica especificacionTecnica) {
        try {
            // Verificar que el sistema existe
            if (especificacionTecnica.getSistema() != null && especificacionTecnica.getSistema().getId() != null) {
                Optional<Sistema> sistema = sistemaRepository.findById(especificacionTecnica.getSistema().getId());
                if (sistema.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                especificacionTecnica.setSistema(sistema.get());

                // Verificar si ya existe una especificación técnica para este sistema
                Optional<EspecificacionTecnica> existingEspecificacion = especificacionTecnicaRepository.findBySistema(sistema.get());
                if (existingEspecificacion.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }

            EspecificacionTecnica newEspecificacionTecnica = especificacionTecnicaRepository.save(especificacionTecnica);
            return new ResponseEntity<>(newEspecificacionTecnica, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar una especificación técnica existente
    @PutMapping("/{id}")
    public ResponseEntity<EspecificacionTecnica> updateEspecificacionTecnica(@PathVariable Long id, @RequestBody EspecificacionTecnica especificacionTecnica) {
        Optional<EspecificacionTecnica> especificacionTecnicaData = especificacionTecnicaRepository.findById(id);

        if (especificacionTecnicaData.isPresent()) {
            EspecificacionTecnica updatedEspecificacionTecnica = especificacionTecnicaData.get();

            updatedEspecificacionTecnica.setTipoCableado(especificacionTecnica.getTipoCableado());
            updatedEspecificacionTecnica.setVoltaje(especificacionTecnica.getVoltaje());
            updatedEspecificacionTecnica.setAmperaje(especificacionTecnica.getAmperaje());
            updatedEspecificacionTecnica.setProtocoloComunicacion(especificacionTecnica.getProtocoloComunicacion());
            updatedEspecificacionTecnica.setPuertosConexion(especificacionTecnica.getPuertosConexion());
            updatedEspecificacionTecnica.setDimensiones(especificacionTecnica.getDimensiones());
            updatedEspecificacionTecnica.setPeso(especificacionTecnica.getPeso());
            updatedEspecificacionTecnica.setTemperaturaOperacion(especificacionTecnica.getTemperaturaOperacion());
            updatedEspecificacionTecnica.setProteccionIp(especificacionTecnica.getProteccionIp());
            updatedEspecificacionTecnica.setCertificaciones(especificacionTecnica.getCertificaciones());
            updatedEspecificacionTecnica.setRequisitosEspeciales(especificacionTecnica.getRequisitosEspeciales());

            return new ResponseEntity<>(especificacionTecnicaRepository.save(updatedEspecificacionTecnica), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar una especificación técnica
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEspecificacionTecnica(@PathVariable Long id) {
        try {
            especificacionTecnicaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Buscar especificación técnica por sistema
    @GetMapping("/buscar/sistema/{idSistema}")
    public ResponseEntity<EspecificacionTecnica> getEspecificacionTecnicaBySistema(@PathVariable Long idSistema) {
        Optional<Sistema> sistema = sistemaRepository.findById(idSistema);
        if (sistema.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<EspecificacionTecnica> especificacionTecnica = especificacionTecnicaRepository.findBySistema(sistema.get());
        return especificacionTecnica.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}