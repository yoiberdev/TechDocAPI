package com.perucontrols.techdoc.repository;

import com.perucontrols.techdoc.model.Componente;
import com.perucontrols.techdoc.model.Sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponenteRepository extends JpaRepository<Componente, Long> {
    List<Componente> findBySistema(Sistema sistema);
    List<Componente> findBySistemaAndEstado(Sistema sistema, Componente.EstadoComponente estado);
    List<Componente> findByNombreContaining(String nombre);
}