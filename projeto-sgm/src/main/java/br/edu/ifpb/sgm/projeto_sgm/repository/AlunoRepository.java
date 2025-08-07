package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    List<Aluno> findAllByCadastradoIsTrue();

    List<Aluno> findAllByDisciplinasPagas_Id(Long disciplinaId);

    List<Aluno> findAllByDisciplinaMonitoria_Id(Long disciplinaId);


    // List<Aluno> findAlunosInscritosNaMonitoria(@Param("monitoriaId") Long monitoriaId);

}