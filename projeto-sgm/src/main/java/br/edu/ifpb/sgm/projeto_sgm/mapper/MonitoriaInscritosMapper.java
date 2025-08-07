package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaInscritosResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;
import br.edu.ifpb.sgm.projeto_sgm.model.Monitoria;
import br.edu.ifpb.sgm.projeto_sgm.model.MonitoriaInscritos;
import br.edu.ifpb.sgm.projeto_sgm.repository.AlunoRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.MonitoriaRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = {AlunoMapper.class} // Não usa mais MonitoriaMapper diretamente aqui
)
public abstract class MonitoriaInscritosMapper {

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private AlunoMapper alunoMapper;

    // Injetamos os mappers individuais em vez do MonitoriaMapper completo
    @Autowired
    private DisciplinaMapper disciplinaMapper;
    @Autowired
    private ProfessorMapper professorMapper;
    @Autowired
    private ProcessoSeletivoMapper processoSeletivoMapper;


    public MonitoriaInscritosResponseDTO toResponseDTO(MonitoriaInscritos entity) {
        if (entity == null) {
            return null;
        }

        // Buscamos o Aluno completo para evitar Lazy Loading
        Aluno alunoCompleto = alunoRepository.findById(entity.getAluno().getId()).orElse(null);

        // Montamos o MonitoriaResponseDTO manualmente aqui dentro
        Monitoria monitoria = entity.getMonitoria();
        MonitoriaResponseDTO monitoriaDTO = new MonitoriaResponseDTO();
        if (monitoria != null) {
            monitoriaDTO.setId(monitoria.getId());
            monitoriaDTO.setNumeroVaga(monitoria.getNumeroVaga());
            monitoriaDTO.setNumeroVagaBolsa(monitoria.getNumeroVagaBolsa());
            monitoriaDTO.setCargaHoraria(monitoria.getCargaHoraria());
            // Usamos os mappers individuais
            monitoriaDTO.setDisciplinaResponseDTO(disciplinaMapper.toResponseDTO(monitoria.getDisciplina()));
            monitoriaDTO.setProfessorResponseDTO(professorMapper.toResponseDTO(monitoria.getProfessor()));
            monitoriaDTO.setProcessoSeletivoResponseDTO(processoSeletivoMapper.toResponseDTO(monitoria.getProcessoSeletivo()));
        }

        MonitoriaInscritosResponseDTO dto = new MonitoriaInscritosResponseDTO();
        dto.setId(entity.getId());
        dto.setSelecionado(entity.isSelecionado());
        dto.setTipoVaga(entity.getTipoVaga());
        dto.setMonitoriaResponseDTO(monitoriaDTO); // DTO da monitoria montado manualmente
        dto.setAlunoResponseDTO(alunoMapper.toResponseDTO(alunoCompleto));

        return dto;
    }
}