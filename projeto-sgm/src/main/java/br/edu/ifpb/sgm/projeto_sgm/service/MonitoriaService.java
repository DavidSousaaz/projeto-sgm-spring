package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import java.util.List;

public interface MonitoriaService {
    MonitoriaResponseDTO save(MonitoriaRequestDTO dto);
    List<MonitoriaResponseDTO> findAll();
    MonitoriaResponseDTO findById(Long id);
    MonitoriaResponseDTO update(Long id, MonitoriaRequestDTO dto);
    void delete(Long id);
}