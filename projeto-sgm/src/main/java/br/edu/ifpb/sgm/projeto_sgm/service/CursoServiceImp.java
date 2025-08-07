package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.CursoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.CursoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.CursoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.InstituicaoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.CursoMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.*;
import br.edu.ifpb.sgm.projeto_sgm.repository.CursoRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.DisciplinaRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.InstituicaoRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CursoServiceImp implements CursoService {

    private final CursoRepository cursoRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final CursoMapper cursoMapper;

    private final ProfessorRepository professorRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final DisciplinaService disciplinaService;

    public CursoServiceImp(CursoRepository cursoRepository, InstituicaoRepository instituicaoRepository, CursoMapper cursoMapper, ProfessorRepository professorRepository, DisciplinaRepository disciplinaRepository, DisciplinaService disciplinaService) {
        this.cursoRepository = cursoRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.cursoMapper = cursoMapper;
        this.professorRepository = professorRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.disciplinaService = disciplinaService;
    }

    @Override
    public CursoResponseDTO save(CursoRequestDTO dto) {

        Instituicao instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + dto.getInstituicaoId() + " não encontrada."));

        Curso curso = cursoMapper.toEntity(dto);
        curso.setInstituicao(instituicao);


        try {
            curso.setNivel(NivelCurso.valueOf(dto.getNivelString().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nível de curso inválido: " + dto.getNivelString());
        }

        Curso savedCurso = cursoRepository.save(curso);
        return cursoMapper.toResponseDTO(savedCurso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponseDTO> findAll() {
        return cursoRepository.findAll().stream()
                .map(cursoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponseDTO findById(Long id) {
        return cursoRepository.findById(id)
                .map(cursoMapper::toResponseDTO)
                .orElseThrow(() -> new CursoNotFoundException("Curso com ID " + id + " não encontrado."));
    }

    @Override
    public CursoResponseDTO update(Long id, CursoRequestDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException("Curso com ID " + id + " não encontrado para atualização."));

        cursoMapper.updateCursoFromDto(dto, curso);

        if (dto.getInstituicaoId() != null) {
            Instituicao instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                    .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + dto.getInstituicaoId() + " não encontrada."));
            curso.setInstituicao(instituicao);
        }

        if (dto.getNivelString() != null && !dto.getNivelString().isBlank()) {
            try {
                curso.setNivel(NivelCurso.valueOf(dto.getNivelString().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Nível de curso inválido: " + dto.getNivelString());
            }
        }

        Curso updatedCurso = cursoRepository.save(curso);
        return cursoMapper.toResponseDTO(updatedCurso);
    }

    @Override
    public void delete(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException("Curso com ID " + id + " não encontrado."));

        List<Professor> coordenadores = professorRepository.findAllByCursos_Id(id);
        coordenadores.forEach(p -> p.getCursos().remove(curso));

        List<Disciplina> disciplinas = disciplinaRepository.findAllByCursoId(id);
        disciplinas.forEach(d -> disciplinaService.delete(d.getId()));

        cursoRepository.delete(curso);
    }
}