package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.model.VersionSoftware;
import com.perucontrols.techdoc.repository.SistemaRepository;
import com.perucontrols.techdoc.repository.VersionSoftwareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/versiones-software")
public class VersionSoftwareController {

    @Autowired
    private VersionSoftwareRepository versionSoftwareRepository;

    @Autowired
    private SistemaRepository sistemaRepository;

    // Obtener todas las versiones de software
    @GetMapping
    public ResponseEntity<List<VersionSoftware>> getAllVersionesSoftware() {
        List<VersionSoftware> versionesSoftware = versionSoftwareRepository.findAll();
        return new ResponseEntity<>(versionesSoftware, HttpStatus.OK);
    }

    // Obtener una versión de software por ID
    @GetMapping("/{id}")
    public ResponseEntity<VersionSoftware> getVersionSoftwareById(@PathVariable Long id) {
        Optional<VersionSoftware> versionSoftware = versionSoftwareRepository.findById(id);
        return versionSoftware.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Crear una nueva versión de software
    @PostMapping
    public ResponseEntity<VersionSoftware> createVersionSoftware(@RequestBody VersionSoftware versionSoftware) {
        try {
            // Verificar que el sistema existe
            if (versionSoftware.getSistema() != null && versionSoftware.getSistema().getId() != null) {
                Optional<Sistema> sistema = sistemaRepository.findById(versionSoftware.getSistema().getId());
                if (sistema.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                versionSoftware.setSistema(sistema.get());
            }

            VersionSoftware newVersionSoftware = versionSoftwareRepository.save(versionSoftware);
            return new ResponseEntity<>(newVersionSoftware, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar una versión de software existente
    @PutMapping("/{id}")
    public ResponseEntity<VersionSoftware> updateVersionSoftware(@PathVariable Long id, @RequestBody VersionSoftware versionSoftware) {
        Optional<VersionSoftware> versionSoftwareData = versionSoftwareRepository.findById(id);

        if (versionSoftwareData.isPresent()) {
            VersionSoftware updatedVersionSoftware = versionSoftwareData.get();

            // Verificar que el sistema existe
            if (versionSoftware.getSistema() != null && versionSoftware.getSistema().getId() != null) {
                Optional<Sistema> sistema = sistemaRepository.findById(versionSoftware.getSistema().getId());
                if (sistema.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                updatedVersionSoftware.setSistema(sistema.get());
            }

            updatedVersionSoftware.setVersion(versionSoftware.getVersion());
            updatedVersionSoftware.setFechaInstalacion(versionSoftware.getFechaInstalacion());
            updatedVersionSoftware.setChangelog(versionSoftware.getChangelog());
            updatedVersionSoftware.setCompatibleCon(versionSoftware.getCompatibleCon());
            updatedVersionSoftware.setArchivoInstalador(versionSoftware.getArchivoInstalador());
            updatedVersionSoftware.setRequisitosSistema(versionSoftware.getRequisitosSistema());
            updatedVersionSoftware.setInstaladoPor(versionSoftware.getInstaladoPor());
            updatedVersionSoftware.setEstado(versionSoftware.getEstado());
            updatedVersionSoftware.setFechaFinSoporte(versionSoftware.getFechaFinSoporte());

            return new ResponseEntity<>(versionSoftwareRepository.save(updatedVersionSoftware), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar una versión de software
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteVersionSoftware(@PathVariable Long id) {
        try {
            versionSoftwareRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Buscar versiones de software por sistema
    @GetMapping("/buscar/sistema/{idSistema}")
    public ResponseEntity<List<VersionSoftware>> getVersionesSoftwareBySistema(@PathVariable Long idSistema) {
        Optional<Sistema> sistema = sistemaRepository.findById(idSistema);
        if (sistema.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<VersionSoftware> versionesSoftware = versionSoftwareRepository.findBySistema(sistema.get());
        return new ResponseEntity<>(versionesSoftware, HttpStatus.OK);
    }

    // Buscar versión de software actual por sistema
    @GetMapping("/buscar/sistema/{idSistema}/actual")
    public ResponseEntity<VersionSoftware> getVersionSoftwareActualBySistema(@PathVariable Long idSistema) {
        Optional<Sistema> sistema = sistemaRepository.findById(idSistema);
        if (sistema.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<VersionSoftware> versionSoftware = versionSoftwareRepository.findBySistemaAndEstado(sistema.get(), VersionSoftware.EstadoVersion.ACTUAL);
        return versionSoftware.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Buscar versiones de software con fin de soporte próximo
    @GetMapping("/buscar/fin-soporte")
    public ResponseEntity<List<VersionSoftware>> getVersionesSoftwarePorFinSoporte() {
        LocalDate fechaActual = LocalDate.now();
        List<VersionSoftware> versionesSoftware = versionSoftwareRepository.findByFechaFinSoporteLessThanEqual(fechaActual);
        return new ResponseEntity<>(versionesSoftware, HttpStatus.OK);
    }
}