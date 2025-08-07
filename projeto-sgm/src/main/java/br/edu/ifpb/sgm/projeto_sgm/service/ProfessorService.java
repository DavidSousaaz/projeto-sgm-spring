package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;

import java.util.List;

public interface ProfessorService {
    ProfessorResponseDTO save(ProfessorRequestDTO dto);
    List<ProfessorResponseDTO> findAll();
    ProfessorResponseDTO findById(Long id);
    ProfessorResponseDTO update(Long id, ProfessorRequestDTO dto);
    void delete(Long id);
    List<MonitoriaResponseDTO> findMonitoriasByProfessor(Pessoa pessoaLogada);
    ProfessorResponseDTO associate(Long pessoaId, ProfessorRequestDTO dto);
}