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

//    List<Monitoria> findByProfessor_Id(Long professorId);
//    List<Monitoria> findByProcessoSeletivo_Id(Long processoId);
//    List<Monitoria> findBySelecionados_Id(Long alunoId);
//    List<Monitoria> findByInscritos_Id(Long alunoId);
//
//    // Retorna Monitorias ativas por instituição e data
//    @Query("SELECT m FROM Monitoria m WHERE m.processoSeletivo.inicio <= :data AND m.processoSeletivo.fim >= :data AND m.processoSeletivo.instituicao.id = :instituicaoId")
//    List<Monitoria> findMonitoriasAtivasPorInstituicao(@Param("data") LocalDate data, @Param("instituicaoId") Long instituicaoId);
//
//    // Relatório de vagas ocupadas
//    @Query("SELECT m.id, m.disciplina, SIZE(m.selecionados) FROM Monitoria m")
//    List<Object[]> countAlunosSelecionadosPorMonitoria();
//
//    //Buscar monitorias por disciplina e status
//    @Query("SELECT m FROM Monitoria m WHERE m.disciplina = :disciplina AND m.processoSeletivo.inicio <= :hoje AND m.processoSeletivo.fim >= :hoje")
//    List<Monitoria> findMonitoriasAtivasPorDisciplina(@Param("disciplina") String disciplina, @Param("hoje") LocalDate hoje);

}
