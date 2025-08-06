package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    List<Disciplina> findAllByCursoId(Long cursoId);



    // List<Disciplina> findByNomeContainingIgnoreCase(String nome);
    //
    // List<Disciplina> findByCursoId(Long cursoId);
}