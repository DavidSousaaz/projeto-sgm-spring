package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.*;
import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;
import java.util.List;

public interface MonitoriaService {
    MonitoriaResponseDTO save(MonitoriaRequestDTO dto);
    // ASSINATURA ATUALIZADA PARA ACEITAR UM FILTRO OPCIONAL
    List<MonitoriaResponseDTO> findAll(Long processoId);
    MonitoriaResponseDTO findById(Long id);
    MonitoriaResponseDTO update(Long id, MonitoriaRequestDTO dto);
    void delete(Long id);
    List<DashboardMonitoriaDTO> getDashboardData();
    MonitoriaInscritosResponseDTO realizarInscricao(Long monitoriaId, Pessoa pessoaLogada, InscricaoRequestDTO inscricaoDTO);
    MonitoriaInscritosResponseDTO selecionarMonitor(Long monitoriaId, Long alunoId);

}