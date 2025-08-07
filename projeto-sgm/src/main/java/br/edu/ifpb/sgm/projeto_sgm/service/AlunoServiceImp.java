package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaInscritosResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.AlunoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.DisciplinaNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.InstituicaoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.PessoaNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.AlunoMapper;
import br.edu.ifpb.sgm.projeto_sgm.mapper.MonitoriaInscritosMapper;
import br.edu.ifpb.sgm.projeto_sgm.mapper.PessoaMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.*;
import br.edu.ifpb.sgm.projeto_sgm.repository.*;
import br.edu.ifpb.sgm.projeto_sgm.util.Constants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlunoServiceImp implements AlunoService {

    private final AlunoRepository alunoRepository;
    private final PessoaRepository pessoaRepository;
    private final RoleRepository roleRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final AlunoMapper alunoMapper;
    private final PessoaMapper pessoaMapper;
    private final PasswordEncoder passwordEncoder;

    private final MonitoriaInscricoesRepository monitoriaInscricoesRepository;
    private final MonitoriaInscritosMapper monitoriaInscritosMapper;

    public AlunoServiceImp(AlunoRepository alunoRepository, PessoaRepository pessoaRepository, RoleRepository roleRepository, DisciplinaRepository disciplinaRepository, InstituicaoRepository instituicaoRepository, AlunoMapper alunoMapper, PessoaMapper pessoaMapper, PasswordEncoder passwordEncoder, MonitoriaInscricoesRepository monitoriaInscricoesRepository, MonitoriaInscritosMapper monitoriaInscritosMapper) {
        this.alunoRepository = alunoRepository;
        this.pessoaRepository = pessoaRepository;
        this.roleRepository = roleRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.alunoMapper = alunoMapper;
        this.pessoaMapper = pessoaMapper;
        this.passwordEncoder = passwordEncoder;
        this.monitoriaInscricoesRepository = monitoriaInscricoesRepository;
        this.monitoriaInscritosMapper = monitoriaInscritosMapper;
    }

    @Override
    public AlunoResponseDTO save(AlunoRequestDTO dto) {

        Pessoa pessoa = pessoaMapper.fromPessoa(dto);
        pessoa.setInstituicao(buscarInstituicao(dto.getInstituicaoId()));
        pessoa.setSenha(passwordEncoder.encode(dto.getSenha()));

        Role alunoRole = roleRepository.findByRole("ROLE_" + Constants.DISCENTE)
                .orElseThrow(() -> new RuntimeException("ERRO CRÍTICO: Role DISCENTE não encontrada no banco!"));
        pessoa.setRoles(List.of(alunoRole));


        Pessoa pessoaSalva = pessoaRepository.save(pessoa);


        Aluno aluno = new Aluno();
        aluno.setPessoa(pessoaSalva);
        aluno.setDisciplinasPagas(buscarDisciplinas(dto.getDisciplinasPagasId()));
        aluno.setDisciplinaMonitoria(buscarDisciplinas(dto.getDisciplinasMonitoriaId()));

        Aluno savedAluno = alunoRepository.save(aluno);
        return alunoMapper.toResponseDTO(savedAluno);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> findAll() {

        return alunoRepository.findAllByCadastradoIsTrue().stream()
                .map(alunoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitoriaInscritosResponseDTO> findInscricoesByAluno(Long alunoId) {
        return monitoriaInscricoesRepository.findAllByAlunoId(alunoId)
                .stream()
                .map(monitoriaInscritosMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AlunoResponseDTO findById(Long id) {
        return alunoRepository.findById(id)
                .map(alunoMapper::toResponseDTO)
                .orElseThrow(() -> new AlunoNotFoundException("Aluno com ID " + id + " não encontrado."));
    }

    @Override
    public AlunoResponseDTO update(Long id, AlunoRequestDTO dto) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoNotFoundException("Aluno com ID " + id + " não encontrado para atualização."));

        Pessoa pessoa = aluno.getPessoa();


        pessoaMapper.updatePessoaFromDto(dto, pessoa);

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            pessoa.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        if (dto.getInstituicaoId() != null) {
            pessoa.setInstituicao(buscarInstituicao(dto.getInstituicaoId()));
        }

        alunoMapper.updateAlunoFromDto(dto, aluno);
        if (dto.getDisciplinasPagasId() != null) {
            aluno.setDisciplinasPagas(buscarDisciplinas(dto.getDisciplinasPagasId()));
        }
        if (dto.getDisciplinasMonitoriaId() != null) {
            aluno.setDisciplinaMonitoria(buscarDisciplinas(dto.getDisciplinasMonitoriaId()));
        }

        Aluno updatedAluno = alunoRepository.save(aluno);
        return alunoMapper.toResponseDTO(updatedAluno);
    }

    @Override
    public void delete(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoNotFoundException("Aluno com ID " + id + " não encontrado para deleção."));

        // Lógica explícita de soft delete
        aluno.setCadastrado(false);
        alunoRepository.save(aluno);
    }

    @Override
    public AlunoResponseDTO associate(Long pessoaId, AlunoRequestDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new PessoaNotFoundException("Pessoa com ID " + pessoaId + " não encontrada para associação."));

        Aluno aluno = new Aluno();
        aluno.setPessoa(pessoa);
        aluno.setDisciplinasPagas(buscarDisciplinas(dto.getDisciplinasPagasId()));
        aluno.setDisciplinaMonitoria(buscarDisciplinas(dto.getDisciplinasMonitoriaId()));

        Aluno savedAluno = alunoRepository.save(aluno);
        return alunoMapper.toResponseDTO(savedAluno);
    }

    private Set<Disciplina> buscarDisciplinas(Set<Long> disciplinaIds) {
        if (disciplinaIds == null || disciplinaIds.isEmpty()) {
            return Collections.emptySet();
        }
        // Otimizado: Uma única consulta ao banco para buscar todas as disciplinas.
        List<Disciplina> disciplinas = disciplinaRepository.findAllById(disciplinaIds);
        if (disciplinas.size() != disciplinaIds.size()) {
            throw new DisciplinaNotFoundException("Uma ou mais IDs de disciplina não foram encontradas.");
        }
        return new HashSet<>(disciplinas);
    }

    private Instituicao buscarInstituicao(Long id) {
        if (id == null) {
            throw new InstituicaoNotFoundException("ID da instituição não pode ser nulo.");
        }
        return instituicaoRepository.findById(id)
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + id + " não encontrada."));
    }
}