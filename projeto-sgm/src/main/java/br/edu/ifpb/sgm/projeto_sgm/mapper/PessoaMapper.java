package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.PessoaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.PessoaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = { InstituicaoMapper.class, RoleMapper.class }
)
public abstract class PessoaMapper {

    @Autowired
    private InstituicaoMapper instituicaoMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public abstract Pessoa fromPessoa(AlunoRequestDTO alunoRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public abstract Pessoa fromPessoa(ProfessorRequestDTO professorRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public abstract Pessoa toEntity(PessoaRequestDTO pessoaRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updatePessoaFromDto(PessoaRequestDTO pessoaRequestDTO, @MappingTarget Pessoa entity);

    // --- ADIÇÃO DOS MÉTODOS CORRIGIDOS AQUI ---

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updatePessoaFromDto(AlunoRequestDTO alunoRequestDTO, @MappingTarget Pessoa entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updatePessoaFromDto(ProfessorRequestDTO professorRequestDTO, @MappingTarget Pessoa entity);


    // --- MÉTODO MANUAL (JÁ ESTAVA CORRETO) ---
    public PessoaResponseDTO toResponseDTO(Pessoa pessoa) {
        if (pessoa == null) return null;
        PessoaResponseDTO dto = new PessoaResponseDTO();
        dto.setId(pessoa.getId());
        dto.setCpf(pessoa.getCpf());
        dto.setNome(pessoa.getNome());
        dto.setEmail(pessoa.getEmail());
        dto.setEmailAcademico(pessoa.getEmailAcademico());
        dto.setMatricula(pessoa.getMatricula());
        if (pessoa.getInstituicao() != null) {
            dto.setInstituicaoResponseDTO(instituicaoMapper.toResponseDTO(pessoa.getInstituicao()));
        }
        if (pessoa.getRoles() != null) {
            dto.setRoles(roleMapper.toDTOList(pessoa.getRoles()));
        }
        return dto;
    }
}