package br.edu.ifpb.sgm.projeto_sgm.model;

import br.edu.ifpb.sgm.projeto_sgm.model.embeddable.MonitoriaInscritoId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "monitoria_inscritos")
public class MonitoriaInscritos {

    @EmbeddedId
    private MonitoriaInscritoId id = new MonitoriaInscritoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("monitoriaId") // Mapeia a parte 'monitoriaId' do EmbeddedId
    @JoinColumn(name = "monitoria_id")
    private Monitoria monitoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("alunoId") // Mapeia a parte 'alunoId' do EmbeddedId
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @Column(nullable = false)
    private boolean selecionado = false;
}