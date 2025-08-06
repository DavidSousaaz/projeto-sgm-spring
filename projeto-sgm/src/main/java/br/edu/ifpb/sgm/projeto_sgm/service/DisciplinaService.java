package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaResponseDTO;
import java.util.List;

public interface DisciplinaService {
    DisciplinaResponseDTO save(DisciplinaRequestDTO dto);
    List<DisciplinaResponseDTO> findAll();
    DisciplinaResponseDTO findById(Long id);
    DisciplinaResponseDTO update(Long id, DisciplinaRequestDTO dto);
    void delete(Long id);
}