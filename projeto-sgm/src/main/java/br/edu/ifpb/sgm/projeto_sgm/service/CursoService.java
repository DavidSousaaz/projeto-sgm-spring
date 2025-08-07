package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.CursoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.CursoResponseDTO;

import java.util.List;

public interface CursoService {
    CursoResponseDTO save(CursoRequestDTO dto);

    List<CursoResponseDTO> findAll();

    CursoResponseDTO findById(Long id);

    CursoResponseDTO update(Long id, CursoRequestDTO dto);

    void delete(Long id);
}