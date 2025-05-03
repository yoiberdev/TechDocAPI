package com.perucontrols.techdoc.repository;

import com.perucontrols.techdoc.model.Mantenimiento;
import com.perucontrols.techdoc.model.Sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {
    List<Mantenimiento> findBySistema(Sistema sistema);
    List<Mantenimiento> findBySistemaAndEstado(Sistema sistema, Mantenimiento.EstadoMantenimiento estado);
    List<Mantenimiento> findByTipo(Mantenimiento.TipoMantenimiento tipo);
    List<Mantenimiento> findByFechaInicioAfterAndFechaInicioBefore(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}