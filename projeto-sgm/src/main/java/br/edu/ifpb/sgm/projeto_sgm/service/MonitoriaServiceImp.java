package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.InscricaoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.*;
import br.edu.ifpb.sgm.projeto_sgm.mapper.MonitoriaMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.*;
import br.edu.ifpb.sgm.projeto_sgm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaInscritosResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.mapper.MonitoriaInscritosMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;

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
    private final MonitoriaMapper monitoriaMapper;
    private final AtividadeRepository atividadeRepository;
    private final MonitoriaInscricoesRepository monitoriaInscricoesRepository;
    private final MonitoriaInscritosMapper monitoriaInscritosMapper;



    public MonitoriaServiceImp(MonitoriaRepository monitoriaRepository, DisciplinaRepository disciplinaRepository, ProfessorRepository professorRepository, ProcessoSeletivoRepository processoSeletivoRepository, AlunoRepository alunoRepository, MonitoriaMapper monitoriaMapper, AtividadeRepository atividadeRepository, MonitoriaInscricoesRepository monitoriaInscricoesRepository, MonitoriaInscritosMapper monitoriaInscritosMapper) {
        this.monitoriaRepository = monitoriaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
        this.processoSeletivoRepository = processoSeletivoRepository;
        this.alunoRepository = alunoRepository;
        this.monitoriaMapper = monitoriaMapper;
        this.atividadeRepository = atividadeRepository;
        this.monitoriaInscricoesRepository = monitoriaInscricoesRepository;
        this.monitoriaInscritosMapper = monitoriaInscritosMapper;
    }

    @Override
    public MonitoriaResponseDTO save(MonitoriaRequestDTO dto) {
        Monitoria monitoria = monitoriaMapper.toEntity(dto);

        // Associa as entidades principais
        monitoria.setDisciplina(buscarDisciplina(dto.getDisciplinaId()));
        monitoria.setProfessor(buscarProfessor(dto.getProfessorId()));
        monitoria.setProcessoSeletivo(buscarProcessoSeletivo(dto.getProcessoSeletivoId()));

        // Salva a monitoria primeiro para ter um ID
        Monitoria monitoriaSalva = monitoriaRepository.save(monitoria);

        // Gerencia as inscrições
        gerenciarInscricoes(monitoriaSalva, dto.getInscricoesId());

        return monitoriaMapper.toResponseDTO(monitoriaRepository.save(monitoriaSalva));
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

        // --- NOVA LÓGICA DE VALIDAÇÃO DE VAGAS ---
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
        // --- FIM DA NOVA LÓGICA ---

        MonitoriaInscritos novaInscricao = new MonitoriaInscritos();
        novaInscricao.setMonitoria(monitoria);
        novaInscricao.setAluno(aluno);
        novaInscricao.setSelecionado(false);
        novaInscricao.setTipoVaga(inscricaoDTO.getTipoVaga()); // Salva a escolha do aluno

        MonitoriaInscritos inscricaoSalva = monitoriaInscricoesRepository.save(novaInscricao);

        return monitoriaInscritosMapper.toResponseDTO(inscricaoSalva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitoriaResponseDTO> findAll() {
        return monitoriaRepository.findAll().stream()
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

        // Gerencia as inscrições, limpando as antigas e adicionando as novas
        if (dto.getInscricoesId() != null) {
            gerenciarInscricoes(monitoria, dto.getInscricoesId());
        }

        Monitoria monitoriaAtualizada = monitoriaRepository.save(monitoria);
        return monitoriaMapper.toResponseDTO(monitoriaAtualizada);
    }

    @Override
    public void delete(Long id) {
        if (!monitoriaRepository.existsById(id)) {
            throw new MonitoriaNotFoundException("Monitoria com ID " + id + " não encontrada para deleção.");
        }

        atividadeRepository.deleteAllByMonitoriaId(id);
        monitoriaInscricoesRepository.deleteAllByMonitoriaId(id);

        monitoriaRepository.deleteById(id);
    }

    // --- MÉTODOS AUXILIARES ---

    public MonitoriaInscritosResponseDTO realizarInscricao(Long monitoriaId, Pessoa pessoaLogada) {
        // 1. Validação: Garante que a pessoa logada é um aluno
        Aluno aluno = alunoRepository.findById(pessoaLogada.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não é um aluno válido."));

        // 2. Validação: Garante que a monitoria existe
        Monitoria monitoria = monitoriaRepository.findById(monitoriaId)
                .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria com ID " + monitoriaId + " não encontrada."));

        // 3. Validação: Verifica se o aluno já está inscrito
        if (monitoriaInscricoesRepository.existsByMonitoriaIdAndAlunoId(monitoriaId, aluno.getId())) {
            throw new RuntimeException("Aluno já inscrito nesta monitoria."); // Idealmente, uma exceção customizada
        }

        // 4. Validação: Verifica se ainda há vagas
        if (monitoria.getInscricoes().size() >= monitoria.getNumeroVaga()) {
            throw new RuntimeException("Não há mais vagas disponíveis para esta monitoria.");
        }

        // 5. Lógica de Negócio: Cria e salva a nova inscrição
        MonitoriaInscritos novaInscricao = new MonitoriaInscritos();
        novaInscricao.setMonitoria(monitoria);
        novaInscricao.setAluno(aluno);
        novaInscricao.setSelecionado(false); // Padrão

        MonitoriaInscritos inscricaoSalva = monitoriaInscricoesRepository.save(novaInscricao);

        return monitoriaInscritosMapper.toResponseDTO(inscricaoSalva);
    }

    private void gerenciarInscricoes(Monitoria monitoria, List<Long> alunoIds) {
        // Limpa as inscrições antigas. Graças ao 'orphanRemoval=true' na entidade Monitoria,
        // isso deletará as entradas correspondentes na tabela 'monitoria_inscritos'.
        monitoria.getInscricoes().clear();

        if (alunoIds != null && !alunoIds.isEmpty()) {
            // Busca todos os alunos em uma única query (otimizado)
            List<Aluno> alunos = alunoRepository.findAllById(alunoIds);
            if (alunos.size() != alunoIds.size()) {
                throw new AlunoNotFoundException("Um ou mais alunos para inscrição não foram encontrados.");
            }

            // Cria as novas entidades de inscrição
            for (Aluno aluno : alunos) {
                MonitoriaInscritos inscricao = new MonitoriaInscritos();
                inscricao.setMonitoria(monitoria);
                inscricao.setAluno(aluno);
                inscricao.setSelecionado(false); // Padrão
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
}