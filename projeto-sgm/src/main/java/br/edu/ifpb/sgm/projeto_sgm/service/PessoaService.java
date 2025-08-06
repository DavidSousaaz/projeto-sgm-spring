package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.PessoaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.PessoaResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface PessoaService extends UserDetailsService {
    // CRUD para Pessoas genéricas (ex: Admin)
    PessoaResponseDTO save(PessoaRequestDTO dto);
    List<PessoaResponseDTO> findAll();
    PessoaResponseDTO findById(Long id);
    PessoaResponseDTO update(Long id, PessoaRequestDTO dto);
    void delete(Long id);

    // Método de utilidade para autenticação
    PessoaResponseDTO findDtoByMatricula(String matricula);
}