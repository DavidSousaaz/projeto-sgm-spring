package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.Monitoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MonitoriaRepository extends JpaRepository<Monitoria, Long> {

    List<Monitoria> findAllByDisciplinaId(Long disciplinaId);

    List<Monitoria> findAllByProcessoSeletivoId(Long processoSeletivoId);

    List<Monitoria> findAllByProfessorId(Long professorId);

}
