package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.InstituicaoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.ProcessoSeletivoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.ProcessoSeletivoMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.Instituicao;
import br.edu.ifpb.sgm.projeto_sgm.model.Monitoria;
import br.edu.ifpb.sgm.projeto_sgm.model.ProcessoSeletivo;
import br.edu.ifpb.sgm.projeto_sgm.repository.InstituicaoRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.MonitoriaRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.ProcessoSeletivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProcessoSeletivoServiceImp implements ProcessoSeletivoService {

    private final ProcessoSeletivoRepository processoSeletivoRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final ProcessoSeletivoMapper processoSeletivoMapper;
    private final MonitoriaRepository monitoriaRepository;
    private final MonitoriaService monitoriaService;

    public ProcessoSeletivoServiceImp(ProcessoSeletivoRepository processoSeletivoRepository, InstituicaoRepository instituicaoRepository, ProcessoSeletivoMapper processoSeletivoMapper, MonitoriaRepository monitoriaRepository, MonitoriaService monitoriaService) {
        this.processoSeletivoRepository = processoSeletivoRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.processoSeletivoMapper = processoSeletivoMapper;
        this.monitoriaRepository = monitoriaRepository;
        this.monitoriaService = monitoriaService;
    }

    @Override
    public ProcessoSeletivoResponseDTO save(ProcessoSeletivoRequestDTO dto) {
        Instituicao instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + dto.getInstituicaoId() + " não encontrada."));

        ProcessoSeletivo processo = processoSeletivoMapper.toEntity(dto);
        processo.setInstituicao(instituicao);

        ProcessoSeletivo savedProcesso = processoSeletivoRepository.save(processo);
        return processoSeletivoMapper.toResponseDTO(savedProcesso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcessoSeletivoResponseDTO> findAll() {
        return processoSeletivoRepository.findAll().stream()
                .map(processoSeletivoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProcessoSeletivoResponseDTO findById(Long id) {
        return processoSeletivoRepository.findById(id)
                .map(processoSeletivoMapper::toResponseDTO)
                .orElseThrow(() -> new ProcessoSeletivoNotFoundException("Processo Seletivo com ID " + id + " não encontrado."));
    }

    @Override
    public ProcessoSeletivoResponseDTO update(Long id, ProcessoSeletivoRequestDTO dto) {
        ProcessoSeletivo processo = processoSeletivoRepository.findById(id)
                .orElseThrow(() -> new ProcessoSeletivoNotFoundException("Processo Seletivo com ID " + id + " não encontrado para atualização."));

        processoSeletivoMapper.updateProcessoSeletivoFromDto(dto, processo);

        if (dto.getInstituicaoId() != null) {
            Instituicao instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                    .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + dto.getInstituicaoId() + " não encontrada."));
            processo.setInstituicao(instituicao);
        }

        ProcessoSeletivo updatedProcesso = processoSeletivoRepository.save(processo);
        return processoSeletivoMapper.toResponseDTO(updatedProcesso);
    }

    @Override
    public void delete(Long id) {
        if (!processoSeletivoRepository.existsById(id)) {
            throw new ProcessoSeletivoNotFoundException("Processo Seletivo com ID " + id + " não encontrado.");
        }

        List<Monitoria> monitorias = monitoriaRepository.findAllByProcessoSeletivoId(id);
        monitorias.forEach(m -> monitoriaService.delete(m.getId()));

        processoSeletivoRepository.deleteById(id);
    }
}