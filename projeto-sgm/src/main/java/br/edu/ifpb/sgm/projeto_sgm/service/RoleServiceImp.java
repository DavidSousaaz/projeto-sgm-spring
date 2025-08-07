package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.RoleRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.RoleResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.RoleNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.RoleMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.Role;
import br.edu.ifpb.sgm.projeto_sgm.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImp(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleResponseDTO save(RoleRequestDTO dto) {
        Role role = roleMapper.toEntity(dto);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDTO(savedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> findAll() {
        return roleRepository.findAll().stream()

                .map(role -> roleMapper.toDTO(role))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDTO findById(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toDTO)
                .orElseThrow(() -> new RoleNotFoundException("Role com ID " + id + " não encontrada."));
    }

    @Override
    public RoleResponseDTO update(Long id, RoleRequestDTO dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role com ID " + id + " não encontrada para atualização."));

        roleMapper.updateEntityFromDTO(dto, role);
        Role updatedRole = roleRepository.save(role);
        return roleMapper.toDTO(updatedRole);
    }

    @Override
    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RoleNotFoundException("Role com ID " + id + " não encontrada para deleção.");
        }
        roleRepository.deleteById(id);
    }
}