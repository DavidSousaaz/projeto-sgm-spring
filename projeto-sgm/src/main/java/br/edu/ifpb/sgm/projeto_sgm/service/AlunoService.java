package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaInscritosResponseDTO;

import java.util.List;

public interface AlunoService {
    AlunoResponseDTO save(AlunoRequestDTO dto);

    List<AlunoResponseDTO> findAll();

    AlunoResponseDTO findById(Long id);

    AlunoResponseDTO update(Long id, AlunoRequestDTO dto);

    void delete(Long id);

    AlunoResponseDTO associate(Long pessoaId, AlunoRequestDTO dto);

    List<MonitoriaInscritosResponseDTO> findInscricoesByAluno(Long alunoId);
}