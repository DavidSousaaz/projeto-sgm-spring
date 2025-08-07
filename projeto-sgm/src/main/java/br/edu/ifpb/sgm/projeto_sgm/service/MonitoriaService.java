package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.InscricaoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaInscritosResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;

import java.util.List;

public interface MonitoriaService {
    MonitoriaResponseDTO save(MonitoriaRequestDTO dto);
    List<MonitoriaResponseDTO> findAll();
    MonitoriaResponseDTO findById(Long id);
    MonitoriaResponseDTO update(Long id, MonitoriaRequestDTO dto);
    void delete(Long id);
    MonitoriaInscritosResponseDTO realizarInscricao(Long monitoriaId, Pessoa pessoaLogada, InscricaoRequestDTO inscricaoDTO);



}