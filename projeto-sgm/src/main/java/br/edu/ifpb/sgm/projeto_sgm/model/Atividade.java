package br.edu.ifpb.sgm.projeto_sgm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "atividade")
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "monitoria_id", nullable = false)
    private Monitoria monitoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAtividade status = StatusAtividade.PENDENTE;
}