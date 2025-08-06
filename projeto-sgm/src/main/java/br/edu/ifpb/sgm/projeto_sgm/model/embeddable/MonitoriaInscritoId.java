package br.edu.ifpb.sgm.projeto_sgm.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitoriaInscritoId implements Serializable {

    @Column(name = "monitoria_id")
    private Long monitoriaId;

    @Column(name = "aluno_id")
    private Long alunoId;

}