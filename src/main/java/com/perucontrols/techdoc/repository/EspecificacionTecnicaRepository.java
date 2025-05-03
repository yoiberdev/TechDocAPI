package com.perucontrols.techdoc.repository;

import com.perucontrols.techdoc.model.EspecificacionTecnica;
import com.perucontrols.techdoc.model.Sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EspecificacionTecnicaRepository extends JpaRepository<EspecificacionTecnica, Long> {
    Optional<EspecificacionTecnica> findBySistema(Sistema sistema);
}