package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findAllByInstituicaoId(Long instituicaoId);

    // Exemplos de métodos de consulta:
    //
    // List<Curso> findByNomeContainingIgnoreCase(String nome);
    //
    // List<Curso> findByInstituicaoId(Long instituicaoId);
}