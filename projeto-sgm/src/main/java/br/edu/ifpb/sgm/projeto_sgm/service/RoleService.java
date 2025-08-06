package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.RoleRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.RoleResponseDTO;
import java.util.List;

public interface RoleService {
    RoleResponseDTO save(RoleRequestDTO dto);
    List<RoleResponseDTO> findAll();
    RoleResponseDTO findById(Long id);
    RoleResponseDTO update(Long id, RoleRequestDTO dto);
    void delete(Long id);
}