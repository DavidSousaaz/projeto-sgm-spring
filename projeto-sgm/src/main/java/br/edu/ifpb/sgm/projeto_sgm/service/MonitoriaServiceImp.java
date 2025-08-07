package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.InscricaoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaInscritosResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.DashboardMonitoriaDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.*;
import br.edu.ifpb.sgm.projeto_sgm.mapper.MonitoriaInscritosMapper;
import br.edu.ifpb.sgm.projeto_sgm.mapper.MonitoriaMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.*;
import br.edu.ifpb.sgm.projeto_sgm.model.embeddable.MonitoriaInscritoId;
import br.edu.ifpb.sgm.projeto_sgm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MonitoriaServiceImp implements MonitoriaService {

    private final MonitoriaRepository monitoriaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;
    private final ProcessoSeletivoRepository processoSeletivoRepository;
    private final AlunoRepository alunoRepository;
    private final AtividadeRepository atividadeRepository;
    private final MonitoriaInscricoesRepository monitoriaInscricoesRepository;
    private final MonitoriaMapper monitoriaMapper;
    private final MonitoriaInscritosMapper monitoriaInscritosMapper;


    public MonitoriaServiceImp(MonitoriaRepository monitoriaRepository, DisciplinaRepository disciplinaRepository, ProfessorRepository professorRepository, ProcessoSeletivoRepository processoSeletivoRepository, AlunoRepository alunoRepository, AtividadeRepository atividadeRepository, MonitoriaInscricoesRepository monitoriaInscricoesRepository, MonitoriaMapper monitoriaMapper, MonitoriaInscritosMapper monitoriaInscritosMapper) {
        this.monitoriaRepository = monitoriaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
        this.processoSeletivoRepository = processoSeletivoRepository;
        this.alunoRepository = alunoRepository;
        this.atividadeRepository = atividadeRepository;
        this.monitoriaInscricoesRepository = monitoriaInscricoesRepository;
        this.monitoriaMapper = monitoriaMapper;
        this.monitoriaInscritosMapper = monitoriaInscritosMapper;
    }

    @Override
    public MonitoriaResponseDTO save(MonitoriaRequestDTO dto) {
        Monitoria monitoria = monitoriaMapper.toEntity(dto);

        monitoria.setDisciplina(buscarDisciplina(dto.getDisciplinaId()));
        monitoria.setProfessor(buscarProfessor(dto.getProfessorId()));
        monitoria.setProcessoSeletivo(buscarProcessoSeletivo(dto.getProcessoSeletivoId()));

        Monitoria monitoriaSalva = monitoriaRepository.save(monitoria);

        gerenciarInscricoes(monitoriaSalva, dto.getInscricoesId());

        return monitoriaMapper.toResponseDTO(monitoriaRepository.save(monitoriaSalva));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitoriaResponseDTO> findAll(Long processoId) {
        List<Monitoria> monitorias;
        if (processoId != null) {
            monitorias = monitoriaRepository.findAllByProcessoSeletivoId(processoId);
        } else {
            monitorias = monitoriaRepository.findAll();
        }
        return monitorias.stream()
                .map(monitoriaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MonitoriaResponseDTO findById(Long id) {
        return monitoriaRepository.findById(id)
                .map(monitoriaMapper::toResponseDTO)
                .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria com ID " + id + " não encontrada."));
    }

    @Override
    public MonitoriaResponseDTO update(Long id, MonitoriaRequestDTO dto) {
        Monitoria monitoria = monitoriaRepository.findById(id)
                .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria com ID " + id + " não encontrada para atualização."));

        monitoriaMapper.updateMonitoriaFromDto(dto, monitoria);

        if (dto.getDisciplinaId() != null) {
            monitoria.setDisciplina(buscarDisciplina(dto.getDisciplinaId()));
        }
        if (dto.getProfessorId() != null) {
            monitoria.setProfessor(buscarProfessor(dto.getProfessorId()));
        }
        if (dto.getProcessoSeletivoId() != null) {
            monitoria.setProcessoSeletivo(buscarProcessoSeletivo(dto.getProcessoSeletivoId()));
        }

        if (dto.getInscricoesId() != null) {
            gerenciarInscricoes(monitoria, dto.getInscricoesId());
        }

        Monitoria monitoriaAtualizada = monitoriaRepository.save(monitoria);
        return monitoriaMapper.toResponseDTO(monitoriaAtualizada);
    }

    @Override
    public void delete(Long id) {
        if (!monitoriaRepository.existsById(id)) {
            throw new MonitoriaNotFoundException("Monitoria com ID " + id + " não encontrada.");
        }

        atividadeRepository.deleteAllByMonitoriaId(id);
        monitoriaInscricoesRepository.deleteAllByMonitoriaId(id);

        monitoriaRepository.deleteById(id);
    }

    @Override
    public MonitoriaInscritosResponseDTO realizarInscricao(Long monitoriaId, Pessoa pessoaLogada, InscricaoRequestDTO inscricaoDTO) {
        Aluno aluno = alunoRepository.findById(pessoaLogada.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não é um aluno válido."));

        Monitoria monitoria = monitoriaRepository.findById(monitoriaId)
                .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria com ID " + monitoriaId + " não encontrada."));

        if (monitoriaInscricoesRepository.existsByMonitoriaIdAndAlunoId(monitoriaId, aluno.getId())) {
            throw new RuntimeException("Aluno já inscrito nesta monitoria.");
        }

        long totalInscritos = monitoria.getInscricoes().size();
        long inscritosBolsa = monitoria.getInscricoes().stream().filter(i -> i.getTipoVaga() == TipoVaga.BOLSA).count();
        int vagasTotais = monitoria.getNumeroVaga();
        int vagasBolsa = monitoria.getNumeroVagaBolsa();

        if (totalInscritos >= vagasTotais) {
            throw new RuntimeException("Não há mais vagas disponíveis para esta monitoria.");
        }
        if (inscricaoDTO.getTipoVaga() == TipoVaga.BOLSA && inscritosBolsa >= vagasBolsa) {
            throw new RuntimeException("Não há mais vagas com bolsa disponíveis para esta monitoria.");
        }

        MonitoriaInscritos novaInscricao = new MonitoriaInscritos();
        novaInscricao.setMonitoria(monitoria);
        novaInscricao.setAluno(aluno);
        novaInscricao.setSelecionado(false);
        novaInscricao.setTipoVaga(inscricaoDTO.getTipoVaga());

        MonitoriaInscritos inscricaoSalva = monitoriaInscricoesRepository.save(novaInscricao);

        return monitoriaInscritosMapper.toResponseDTO(inscricaoSalva);
    }

    @Override
    public MonitoriaInscritosResponseDTO selecionarMonitor(Long monitoriaId, Long alunoId) {
        MonitoriaInscritoId inscricaoId = new MonitoriaInscritoId(monitoriaId, alunoId);

        MonitoriaInscritos inscricao = monitoriaInscricoesRepository.findById(inscricaoId)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada."));

        Monitoria monitoria = inscricao.getMonitoria();

        // Lógica de negócio: verifica se ainda há vagas com bolsa para preencher
        long selecionadosBolsa = monitoria.getInscricoes().stream()
                .filter(i -> i.isSelecionado() && i.getTipoVaga() == TipoVaga.BOLSA)
                .count();

        // Se o aluno se inscreveu para bolsa, mas as vagas de bolsa já foram preenchidas,
        // ele é selecionado como voluntário.
        if (inscricao.getTipoVaga() == TipoVaga.BOLSA && selecionadosBolsa >= monitoria.getNumeroVagaBolsa()) {
            inscricao.setTipoVaga(TipoVaga.VOLUNTARIA);
            // Poderia adicionar uma notificação/lógica extra aqui
        }

        inscricao.setSelecionado(true);
        MonitoriaInscritos inscricaoSalva = monitoriaInscricoesRepository.save(inscricao);

        return monitoriaInscritosMapper.toResponseDTO(inscricaoSalva);
    }

    private void gerenciarInscricoes(Monitoria monitoria, List<Long> alunoIds) {
        monitoria.getInscricoes().clear();

        if (alunoIds != null && !alunoIds.isEmpty()) {
            List<Aluno> alunos = alunoRepository.findAllById(alunoIds);
            if (alunos.size() != alunoIds.size()) {
                throw new AlunoNotFoundException("Um ou mais alunos para inscrição não foram encontrados.");
            }

            for (Aluno aluno : alunos) {
                MonitoriaInscritos inscricao = new MonitoriaInscritos();
                inscricao.setMonitoria(monitoria);
                inscricao.setAluno(aluno);
                inscricao.setSelecionado(false);
                // Por padrão, uma inscrição gerenciada por admin/coord pode ser VOLUNTARIA
                inscricao.setTipoVaga(TipoVaga.VOLUNTARIA);
                monitoria.getInscricoes().add(inscricao);
            }
        }
    }

    private Disciplina buscarDisciplina(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com ID " + id + " não encontrada."));
    }

    private Professor buscarProfessor(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + id + " não encontrado."));
    }

    private ProcessoSeletivo buscarProcessoSeletivo(Long id) {
        return processoSeletivoRepository.findById(id)
                .orElseThrow(() -> new ProcessoSeletivoNotFoundException("Processo Seletivo com ID " + id + " não encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DashboardMonitoriaDTO> getDashboardData() {
        return monitoriaRepository.findAll().stream()
                .map(monitoriaMapper::toDashboardDTO)
                .collect(Collectors.toList());
    }
}