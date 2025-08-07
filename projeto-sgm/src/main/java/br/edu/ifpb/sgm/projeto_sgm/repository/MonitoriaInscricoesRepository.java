package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.MonitoriaInscritos;
import br.edu.ifpb.sgm.projeto_sgm.model.embeddable.MonitoriaInscritoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoriaInscricoesRepository extends JpaRepository<MonitoriaInscritos, MonitoriaInscritoId> {
    void deleteAllByMonitoriaId(Long monitoriaId);

    List<MonitoriaInscritos> findAllByAlunoId(Long alunoId);

    boolean existsByMonitoriaIdAndAlunoId(Long monitoriaId, Long alunoId);
}