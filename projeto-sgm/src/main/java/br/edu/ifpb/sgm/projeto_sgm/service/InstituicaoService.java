package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.InstituicaoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.InstituicaoResponseDTO;

import java.util.List;

public interface InstituicaoService {
    InstituicaoResponseDTO save(InstituicaoRequestDTO dto);

    List<InstituicaoResponseDTO> findAll();

    InstituicaoResponseDTO findById(Long id);

    InstituicaoResponseDTO update(Long id, InstituicaoRequestDTO dto);

    void delete(Long id);
}