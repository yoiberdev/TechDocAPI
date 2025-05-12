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
import java.util.Optional;

@Repository
public interface SistemaRepository extends JpaRepository<Sistema, Long> {

    List<Sistema> findByEmbarcacion(Embarcacion embarcacion);

    List<Sistema> findByTipoSistema(TipoSistema tipoSistema);

    List<Sistema> findByEstado(Sistema.EstadoSistema estado);

    List<Sistema> findByNombreContaining(String nombre);

    Optional<Sistema> findByNumeroSerieAndEmbarcacion(String numeroSerie, Embarcacion embarcacion);

    @Query("SELECT s FROM Sistema s WHERE s.fechaProximoMantenimiento <= :fechaActual OR " +
            "(s.tiempoVidaRestante IS NOT NULL AND s.tiempoVidaRestante <= 0)")
    List<Sistema> findSistemasRequiringMaintenance(@Param("fechaActual") LocalDate fechaActual);
}