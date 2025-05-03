package com.perucontrols.techdoc.repository;

import com.perucontrols.techdoc.model.Embarcacion;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.model.TipoSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SistemaRepository extends JpaRepository<Sistema, Long> {
    List<Sistema> findByEmbarcacion(Embarcacion embarcacion);
    List<Sistema> findByTipoSistema(TipoSistema tipoSistema);
    List<Sistema> findByEstado(Sistema.EstadoSistema estado);
    List<Sistema> findByNombreContaining(String nombre);
    
    @Query("SELECT s FROM Sistema s WHERE s.fechaProximoMantenimiento <= :fecha OR s.fechaProximoMantenimiento IS NULL")
    List<Sistema> findSistemasRequiringMaintenance(@Param("fecha") LocalDate fecha);
}