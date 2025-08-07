package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoResponseDTO;

import java.util.List;

public interface ProcessoSeletivoService {
    ProcessoSeletivoResponseDTO save(ProcessoSeletivoRequestDTO dto);

    List<ProcessoSeletivoResponseDTO> findAll();

    ProcessoSeletivoResponseDTO findById(Long id);

    ProcessoSeletivoResponseDTO update(Long id, ProcessoSeletivoRequestDTO dto);

    void delete(Long id);
}