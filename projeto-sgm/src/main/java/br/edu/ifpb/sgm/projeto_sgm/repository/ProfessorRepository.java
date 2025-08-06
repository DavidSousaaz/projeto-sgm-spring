package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;
import br.edu.ifpb.sgm.projeto_sgm.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    List<Professor> findAllByCadastradoIsTrue();

    List<Professor> findAllByCursos_Id(Long cursoId);

    List<Professor> findAllByDisciplinas_Id(Long disciplinaId);

//    List<Professor> findByNomeContainingIgnoreCase(String nome);
//    List<Professor> findByInstituicao_Id(Long instituicaoId);
}
