package com.smartcampus.msAsignatura.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SemestreResponseDTO {
    
    private Long idSemestre;
    private String nombre_semestre;


}
