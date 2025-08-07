package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;

@Data
public class DashboardMonitoriaDTO {
    private Long id;
    private String nomeDisciplina;
    private String nomeProfessor;
    private Integer vagasOcupadas;
    private Integer vagasTotais;
}