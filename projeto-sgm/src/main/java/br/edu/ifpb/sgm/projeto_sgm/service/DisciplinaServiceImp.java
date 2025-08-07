package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.CursoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.DisciplinaNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.DisciplinaMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.*;
import br.edu.ifpb.sgm.projeto_sgm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DisciplinaServiceImp implements DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;
    private final DisciplinaMapper disciplinaMapper;
    private final MonitoriaRepository monitoriaRepository;
    private final MonitoriaService monitoriaService;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;

    public DisciplinaServiceImp(DisciplinaRepository disciplinaRepository, CursoRepository cursoRepository, DisciplinaMapper disciplinaMapper, MonitoriaRepository monitoriaRepository, MonitoriaService monitoriaService, ProfessorRepository professorRepository, AlunoRepository alunoRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.cursoRepository = cursoRepository;
        this.disciplinaMapper = disciplinaMapper;
        this.monitoriaRepository = monitoriaRepository;
        this.monitoriaService = monitoriaService;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
    }

    @Override
    public DisciplinaResponseDTO save(DisciplinaRequestDTO dto) {

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new CursoNotFoundException("Curso com ID " + dto.getCursoId() + " não encontrado."));

        Disciplina disciplina = disciplinaMapper.toEntity(dto);
        disciplina.setCurso(curso);

        Disciplina savedDisciplina = disciplinaRepository.save(disciplina);
        return disciplinaMapper.toResponseDTO(savedDisciplina);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisciplinaResponseDTO> findAll() {
        return disciplinaRepository.findAll().stream()
                .map(disciplinaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DisciplinaResponseDTO findById(Long id) {
        return disciplinaRepository.findById(id)
                .map(disciplinaMapper::toResponseDTO)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com ID " + id + " não encontrada."));
    }

    @Override
    public DisciplinaResponseDTO update(Long id, DisciplinaRequestDTO dto) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com ID " + id + " não encontrada para atualização."));

        disciplinaMapper.updateDisciplinaFromDto(dto, disciplina);

        // Se o DTO de atualização inclui um novo cursoId, busca e atualiza a associação
        if (dto.getCursoId() != null) {
            Curso curso = cursoRepository.findById(dto.getCursoId())
                    .orElseThrow(() -> new CursoNotFoundException("Curso com ID " + dto.getCursoId() + " não encontrado."));
            disciplina.setCurso(curso);
        }

        Disciplina updatedDisciplina = disciplinaRepository.save(disciplina);
        return disciplinaMapper.toResponseDTO(updatedDisciplina);
    }

    @Override
    public void delete(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com ID " + id + " não encontrada."));

        List<Monitoria> monitorias = monitoriaRepository.findAllByDisciplinaId(id);
        monitorias.forEach(monitoria -> monitoriaService.delete(monitoria.getId()));

        List<Professor> professores = professorRepository.findAllByDisciplinas_Id(id);
        professores.forEach(p -> p.getDisciplinas().remove(disciplina));

        List<Aluno> alunosComDisciplinaPaga = alunoRepository.findAllByDisciplinasPagas_Id(id);
        alunosComDisciplinaPaga.forEach(a -> a.getDisciplinasPagas().remove(disciplina));

        List<Aluno> alunosMonitores = alunoRepository.findAllByDisciplinaMonitoria_Id(id);
        alunosMonitores.forEach(a -> a.getDisciplinaMonitoria().remove(disciplina));

        disciplinaRepository.delete(disciplina);
    }
}