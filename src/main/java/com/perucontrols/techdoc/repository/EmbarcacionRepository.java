package com.perucontrols.techdoc.repository;

import com.perucontrols.techdoc.model.Embarcacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmbarcacionRepository extends JpaRepository<Embarcacion, Long> {
    Optional<Embarcacion> findByMatricula(String matricula);
    List<Embarcacion> findByEmpresaPropietaria(String empresaPropietaria);
    List<Embarcacion> findByEstado(Embarcacion.EstadoEmbarcacion estado);
}