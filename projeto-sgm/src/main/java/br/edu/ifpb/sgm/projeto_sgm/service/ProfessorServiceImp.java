package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.*;
import br.edu.ifpb.sgm.projeto_sgm.mapper.MonitoriaMapper;
import br.edu.ifpb.sgm.projeto_sgm.mapper.PessoaMapper;
import br.edu.ifpb.sgm.projeto_sgm.mapper.ProfessorMapper;
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
public class ProfessorServiceImp implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final PessoaRepository pessoaRepository;
    private final RoleRepository roleRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final ProfessorMapper professorMapper;
    private final PessoaMapper pessoaMapper;
    private final PasswordEncoder passwordEncoder;

    private final MonitoriaRepository monitoriaRepository;
    private final MonitoriaMapper monitoriaMapper;

    public ProfessorServiceImp(ProfessorRepository professorRepository, PessoaRepository pessoaRepository, RoleRepository roleRepository, DisciplinaRepository disciplinaRepository, CursoRepository cursoRepository, InstituicaoRepository instituicaoRepository, ProfessorMapper professorMapper, PessoaMapper pessoaMapper, PasswordEncoder passwordEncoder, MonitoriaRepository monitoriaRepository, MonitoriaMapper monitoriaMapper) {
        this.professorRepository = professorRepository;
        this.pessoaRepository = pessoaRepository;
        this.roleRepository = roleRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.cursoRepository = cursoRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.professorMapper = professorMapper;
        this.pessoaMapper = pessoaMapper;
        this.passwordEncoder = passwordEncoder;
        this.monitoriaRepository = monitoriaRepository;
        this.monitoriaMapper = monitoriaMapper;
    }

    @Override
    public ProfessorResponseDTO save(ProfessorRequestDTO dto) {
        Pessoa pessoa = pessoaMapper.fromPessoa(dto);
        pessoa.setInstituicao(buscarInstituicao(dto.getInstituicaoId()));
        pessoa.setSenha(passwordEncoder.encode(dto.getSenha()));

        Role professorRole = roleRepository.findByRole("ROLE_" + Constants.DOCENTE)
                .orElseThrow(() -> new RuntimeException("ERRO CRÍTICO: Role DOCENTE não encontrada no banco!"));
        pessoa.setRoles(List.of(professorRole));

        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        Professor professor = new Professor();
        professor.setPessoa(pessoaSalva);
        professor.setDisciplinas(buscarDisciplinas(new HashSet<>(dto.getDisciplinasId())));
        professor.setCursos(buscarCursos(dto.getCursosId()));

        Professor salvo = professorRepository.save(professor);
        return professorMapper.toResponseDTO(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessorResponseDTO> findAll() {
        // Altere a chamada para o novo método
        return professorRepository.findAllByCadastradoIsTrue().stream()
                .map(professorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessorResponseDTO findById(Long id) {
        return professorRepository.findById(id)
                .map(professorMapper::toResponseDTO)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + id + " não encontrado."));
    }

    @Override
    public ProfessorResponseDTO update(Long id, ProfessorRequestDTO dto) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + id + " não encontrado para atualização."));

        Pessoa pessoa = professor.getPessoa();

        // CORREÇÃO: Chamada mais simples e direta.
        // Em vez de: pessoaMapper.updatePessoaFromPessoa(pessoaMapper.fromPessoa(dto), pessoa);
        // Usamos:
        pessoaMapper.updatePessoaFromDto(dto, pessoa);

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            pessoa.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        if (dto.getInstituicaoId() != null) {
            pessoa.setInstituicao(buscarInstituicao(dto.getInstituicaoId()));
        }

        professorMapper.updateProfessorFromDto(dto, professor);
        if (dto.getDisciplinasId() != null) {
            professor.setDisciplinas(buscarDisciplinas(new HashSet<>(dto.getDisciplinasId())));
        }
        if (dto.getCursosId() != null) {
            professor.setCursos(buscarCursos(dto.getCursosId()));
        }

        Professor atualizado = professorRepository.save(professor);
        return professorMapper.toResponseDTO(atualizado);
    }

    @Override
    public void delete(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + id + " não encontrado para deleção."));

        // Lógica explícita de soft delete
        professor.setCadastrado(false);
        professorRepository.save(professor);
    }

    @Override
    public ProfessorResponseDTO associate(Long pessoaId, ProfessorRequestDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new PessoaNotFoundException("Pessoa com ID " + pessoaId + " não encontrada para associação."));

        Professor professor = new Professor();
        professor.setPessoa(pessoa);
        professor.setDisciplinas(buscarDisciplinas(new HashSet<>(dto.getDisciplinasId())));
        professor.setCursos(buscarCursos(dto.getCursosId()));

        Professor salvo = professorRepository.save(professor);
        return professorMapper.toResponseDTO(salvo);
    }

    private Set<Disciplina> buscarDisciplinas(Set<Long> disciplinaIds) {
        if (disciplinaIds == null || disciplinaIds.isEmpty()) return Collections.emptySet();
        List<Disciplina> disciplinas = disciplinaRepository.findAllById(disciplinaIds);
        if (disciplinas.size() != disciplinaIds.size()) {
            throw new DisciplinaNotFoundException("Um ou mais IDs de disciplina não foram encontrados.");
        }
        return new HashSet<>(disciplinas);
    }

    private Set<Curso> buscarCursos(Set<Long> cursoIds) {
        if (cursoIds == null || cursoIds.isEmpty()) return Collections.emptySet();
        List<Curso> cursos = cursoRepository.findAllById(cursoIds);
        if (cursos.size() != cursoIds.size()) {
            throw new CursoNotFoundException("Um ou mais IDs de curso não foram encontrados.");
        }
        return new HashSet<>(cursos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitoriaResponseDTO> findMonitoriasByProfessor(Pessoa pessoaLogada) {
        return monitoriaRepository.findAllByProfessorId(pessoaLogada.getId())
                .stream()
                .map(monitoriaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private Instituicao buscarInstituicao(Long id) {
        if (id == null) {
            throw new InstituicaoNotFoundException("ID da instituição não pode ser nulo.");
        }
        return instituicaoRepository.findById(id)
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + id + " não encontrada."));
    }
}