package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.VersionSoftware;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVersionSoftwareRequest {
    @NotNull
    private Long idSistema;

    @NotBlank
    private String version;

    @NotNull
    private LocalDate fechaInstalacion;

    private String changelog;
    private String compatibleCon;
    private String archivoInstalador;
    private String requisitosSistema;
    private String instaladoPor;
    private VersionSoftware.EstadoVersion estado;
    private LocalDate fechaFinSoporte;
}
