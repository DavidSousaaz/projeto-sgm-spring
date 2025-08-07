package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.DashboardMonitoriaDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaInscritosResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Monitoria;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        // O MonitoriaInscritosMapper ainda é usado aqui, mas agora ele não depende mais de volta do MonitoriaMapper
        uses = {DisciplinaMapper.class, ProfessorMapper.class, ProcessoSeletivoMapper.class, MonitoriaInscritosMapper.class}
)
public abstract class MonitoriaMapper {

    // A injeção @Lazy não é mais necessária, o ciclo foi quebrado
    @Autowired
    protected MonitoriaInscritosMapper monitoriaInscritosMapper;

    @Autowired
    protected DisciplinaMapper disciplinaMapper;
    @Autowired
    protected ProfessorMapper professorMapper;
    @Autowired
    protected ProcessoSeletivoMapper processoSeletivoMapper;


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "disciplina", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "processoSeletivo", ignore = true)
    @Mapping(target = "inscricoes", ignore = true)
    public abstract Monitoria toEntity(MonitoriaRequestDTO monitoriaRequestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateMonitoriaFromDto(MonitoriaRequestDTO dto, @MappingTarget Monitoria entity);

    @Mapping(source = "disciplina.nome", target = "nomeDisciplina")
    @Mapping(source = "professor.pessoa.nome", target = "nomeProfessor")
    @Mapping(source = "numeroVaga", target = "vagasTotais")
    @Mapping(target = "vagasOcupadas", expression = "java(monitoria.getInscricoes().size())")
    public abstract DashboardMonitoriaDTO toDashboardDTO(Monitoria monitoria);

    public MonitoriaResponseDTO toResponseDTO(Monitoria monitoria) {
        if (monitoria == null) {
            return null;
        }

        MonitoriaResponseDTO dto = new MonitoriaResponseDTO();

        dto.setId(monitoria.getId());
        dto.setNumeroVaga(monitoria.getNumeroVaga());
        dto.setNumeroVagaBolsa(monitoria.getNumeroVagaBolsa());
        dto.setCargaHoraria(monitoria.getCargaHoraria());

        if (monitoria.getDisciplina() != null) {
            dto.setDisciplinaResponseDTO(disciplinaMapper.toResponseDTO(monitoria.getDisciplina()));
        }
        if (monitoria.getProfessor() != null) {
            dto.setProfessorResponseDTO(professorMapper.toResponseDTO(monitoria.getProfessor()));
        }
        if (monitoria.getProcessoSeletivo() != null) {
            dto.setProcessoSeletivoResponseDTO(processoSeletivoMapper.toResponseDTO(monitoria.getProcessoSeletivo()));
        }

        if (monitoria.getInscricoes() != null) {
            List<MonitoriaInscritosResponseDTO> inscritosDTO = monitoria.getInscricoes()
                    .stream()
                    .map(monitoriaInscritosMapper::toResponseDTO)
                    .collect(Collectors.toList());
            dto.setMonitoriaInscritosResponseDTO(inscritosDTO);
        }

        return dto;
    }
}