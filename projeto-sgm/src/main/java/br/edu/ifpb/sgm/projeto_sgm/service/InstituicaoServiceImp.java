package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.InstituicaoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.InstituicaoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.InstituicaoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.InstituicaoMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.Instituicao;
import br.edu.ifpb.sgm.projeto_sgm.repository.InstituicaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InstituicaoServiceImp implements InstituicaoService {

    private final InstituicaoRepository instituicaoRepository;
    private final InstituicaoMapper instituicaoMapper;

    // Injeção de dependências via construtor (melhor prática)
    public InstituicaoServiceImp(InstituicaoRepository instituicaoRepository, InstituicaoMapper instituicaoMapper) {
        this.instituicaoRepository = instituicaoRepository;
        this.instituicaoMapper = instituicaoMapper;
    }

    @Override
    public InstituicaoResponseDTO save(InstituicaoRequestDTO dto) {
        Instituicao instituicao = instituicaoMapper.toEntity(dto);
        Instituicao savedInstituicao = instituicaoRepository.save(instituicao);
        return instituicaoMapper.toResponseDTO(savedInstituicao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstituicaoResponseDTO> findAll() {
        return instituicaoRepository.findAll()
                .stream()
                .map(instituicaoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InstituicaoResponseDTO findById(Long id) {
        return instituicaoRepository.findById(id)
                .map(instituicaoMapper::toResponseDTO)
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + id + " não encontrada."));
    }

    @Override
    public InstituicaoResponseDTO update(Long id, InstituicaoRequestDTO dto) {
        Instituicao instituicao = instituicaoRepository.findById(id)
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + id + " não encontrada para atualização."));

        instituicaoMapper.updateInstituicaoFromDto(dto, instituicao);
        Instituicao updatedInstituicao = instituicaoRepository.save(instituicao);
        return instituicaoMapper.toResponseDTO(updatedInstituicao);
    }

    @Override
    public void delete(Long id) {
        if (!instituicaoRepository.existsById(id)) {
            throw new InstituicaoNotFoundException("Instituição com ID " + id + " não encontrada para deleção.");
        }
        instituicaoRepository.deleteById(id);
    }
}