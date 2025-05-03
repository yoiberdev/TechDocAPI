package com.perucontrols.techdoc.repository;

import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.model.VersionSoftware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VersionSoftwareRepository extends JpaRepository<VersionSoftware, Long> {
    List<VersionSoftware> findBySistema(Sistema sistema);
    Optional<VersionSoftware> findBySistemaAndEstado(Sistema sistema, VersionSoftware.EstadoVersion estado);
    List<VersionSoftware> findByFechaFinSoporteLessThanEqual(LocalDate fecha);
}