package com.diego.Ms_Gestion_Lista.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListaResponseDTO {
    private Long idLista;
    private Long idUser;
    private Long idCurso;
    private LocalDateTime fechaCreacion;
}
