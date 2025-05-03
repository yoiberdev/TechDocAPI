package com.perucontrols.techdoc.repository;

import com.perucontrols.techdoc.model.Documentacion;
import com.perucontrols.techdoc.model.Sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentacionRepository extends JpaRepository<Documentacion, Long> {
    List<Documentacion> findBySistema(Sistema sistema);
    List<Documentacion> findByTipoDocumento(Documentacion.TipoDocumento tipoDocumento);

    @Query("SELECT d FROM Documentacion d WHERE d.tags LIKE %:tag%")
    List<Documentacion> findByTagsContaining(String tag);


    List<Documentacion> findByTituloContaining(String titulo);
}