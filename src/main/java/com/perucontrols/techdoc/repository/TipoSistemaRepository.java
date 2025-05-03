package com.perucontrols.techdoc.repository;

import com.perucontrols.techdoc.model.TipoSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoSistemaRepository extends JpaRepository<TipoSistema, Long> {
    List<TipoSistema> findByCategoria(TipoSistema.CategoriaSistema categoria);
    List<TipoSistema> findByNombreContaining(String nombre);
}