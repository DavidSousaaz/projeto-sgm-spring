package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.AtividadeRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AtividadeResponseDTO;
import java.util.List;

public interface AtividadeService {
    AtividadeResponseDTO save(AtividadeRequestDTO dto);
    List<AtividadeResponseDTO> findAll(Long monitoriaId);
    AtividadeResponseDTO findById(Long id);
    AtividadeResponseDTO update(Long id, AtividadeRequestDTO dto);
    void delete(Long id);
}