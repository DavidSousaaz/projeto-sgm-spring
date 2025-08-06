package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorResponseDTO;
import java.util.List;

public interface ProfessorService {
    ProfessorResponseDTO save(ProfessorRequestDTO dto);
    List<ProfessorResponseDTO> findAll();
    ProfessorResponseDTO findById(Long id);
    ProfessorResponseDTO update(Long id, ProfessorRequestDTO dto);
    void delete(Long id);
    ProfessorResponseDTO associate(Long pessoaId, ProfessorRequestDTO dto);
}