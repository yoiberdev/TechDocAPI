package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.VersionSoftware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionSoftwareDTO {
    private Long id;
    private Long idSistema;
    private String version;
    private LocalDate fechaInstalacion;
    private String changelog;
    private String compatibleCon;
    private String archivoInstalador;
    private String requisitosSistema;
    private String instaladoPor;
    private VersionSoftware.EstadoVersion estado;
    private LocalDate fechaFinSoporte;
}
