package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.Documentacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDocumentacionRequest {
    private Long idSistema;
    private Documentacion.TipoDocumento tipoDocumento;
    private String titulo;
    private String archivo;
    private LocalDate fechaCreacion;
    private String creadoPor;
    private String version;
    private String descripcion;
    private String tags;
}
