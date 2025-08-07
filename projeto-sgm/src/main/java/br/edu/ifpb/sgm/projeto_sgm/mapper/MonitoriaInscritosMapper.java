package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaInscritosResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;
import br.edu.ifpb.sgm.projeto_sgm.model.Monitoria;
import br.edu.ifpb.sgm.projeto_sgm.model.MonitoriaInscritos;
import br.edu.ifpb.sgm.projeto_sgm.repository.AlunoRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.MonitoriaRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

// 1. Transformado em classe abstrata
@Mapper(
        componentModel = "spring",
        uses = {AlunoMapper.class, MonitoriaMapper.class}
)
public abstract class MonitoriaInscritosMapper {

    // 2. Injetando dependências para buscar os dados completos
    @Autowired
    private MonitoriaRepository monitoriaRepository;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private MonitoriaMapper monitoriaMapper;
    @Autowired
    private AlunoMapper alunoMapper;

    // 3. Implementando o método manualmente para controlar o carregamento
    public MonitoriaInscritosResponseDTO toResponseDTO(MonitoriaInscritos entity) {
        if (entity == null) {
            return null;
        }

        // 4. Buscando as entidades completas do banco para evitar o Lazy Loading
        Monitoria monitoriaCompleta = monitoriaRepository.findById(entity.getMonitoria().getId()).orElse(null);
        Aluno alunoCompleto = alunoRepository.findById(entity.getAluno().getId()).orElse(null);

        MonitoriaInscritosResponseDTO dto = new MonitoriaInscritosResponseDTO();

        // 5. Mapeando os dados completos
        dto.setId(entity.getId());
        dto.setSelecionado(entity.isSelecionado());
        dto.setTipoVaga(entity.getTipoVaga());
        dto.setMonitoriaResponseDTO(monitoriaMapper.toResponseDTO(monitoriaCompleta));
        dto.setAlunoResponseDTO(alunoMapper.toResponseDTO(alunoCompleto));

        return dto;
    }
}