package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.AtividadeRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AtividadeResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.AtividadeNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.MonitoriaNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.AtividadeMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.Atividade;
import br.edu.ifpb.sgm.projeto_sgm.model.Monitoria;
import br.edu.ifpb.sgm.projeto_sgm.repository.AtividadeRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.MonitoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; // Import necessário
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AtividadeServiceImp implements AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final MonitoriaRepository monitoriaRepository;
    private final AtividadeMapper atividadeMapper;

    public AtividadeServiceImp(AtividadeRepository atividadeRepository, MonitoriaRepository monitoriaRepository, AtividadeMapper atividadeMapper) {
        this.atividadeRepository = atividadeRepository;
        this.monitoriaRepository = monitoriaRepository;
        this.atividadeMapper = atividadeMapper;
    }

    @Override
    public AtividadeResponseDTO save(AtividadeRequestDTO dto) {
        Monitoria monitoria = monitoriaRepository.findById(dto.getMonitoriaId())
                .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria com ID " + dto.getMonitoriaId() + " não encontrada."));

        Atividade atividade = atividadeMapper.toEntity(dto);

        // --- CORREÇÃO DEFINITIVA AQUI ---
        // Se o front-end não enviar uma data, o back-end gera a data e hora atuais.
        if (atividade.getDataHora() == null) {
            atividade.setDataHora(LocalDateTime.now());
        }

        atividade.setMonitoria(monitoria);

        Atividade savedAtividade = atividadeRepository.save(atividade);
        return atividadeMapper.toResponseDTO(savedAtividade);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtividadeResponseDTO> findAll(Long monitoriaId) {
        List<Atividade> atividades;
        if (monitoriaId != null) {
            atividades = atividadeRepository.findAllByMonitoriaId(monitoriaId);
        } else {
            atividades = atividadeRepository.findAll();
        }
        return atividades.stream()
                .map(atividadeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AtividadeResponseDTO findById(Long id) {
        return atividadeRepository.findById(id)
                .map(atividadeMapper::toResponseDTO)
                .orElseThrow(() -> new AtividadeNotFoundException("Atividade com ID " + id + " não encontrada."));
    }

    @Override
    public AtividadeResponseDTO update(Long id, AtividadeRequestDTO dto) {
        Atividade atividade = atividadeRepository.findById(id)
                .orElseThrow(() -> new AtividadeNotFoundException("Atividade com ID " + id + " não encontrada para atualização."));

        atividadeMapper.updateAtividadeFromDto(dto, atividade);

        if (dto.getMonitoriaId() != null) {
            Monitoria monitoria = monitoriaRepository.findById(dto.getMonitoriaId())
                    .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria com ID " + dto.getMonitoriaId() + " não encontrada."));
            atividade.setMonitoria(monitoria);
        }

        Atividade updatedAtividade = atividadeRepository.save(atividade);
        return atividadeMapper.toResponseDTO(updatedAtividade);
    }

    @Override
    public void delete(Long id) {
        if (!atividadeRepository.existsById(id)) {
            throw new AtividadeNotFoundException("Atividade com ID " + id + " não encontrada para deleção.");
        }
        atividadeRepository.deleteById(id);
    }
}