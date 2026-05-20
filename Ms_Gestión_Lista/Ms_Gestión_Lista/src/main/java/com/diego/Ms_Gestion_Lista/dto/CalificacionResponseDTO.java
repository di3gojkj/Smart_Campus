package com.diego.Ms_Gestion_Lista.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalificacionResponseDTO {
    private Long idCalificacion;
    private BigDecimal nota;
    private Long idLista;
    private Long idCurEva;
}
