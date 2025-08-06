package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Optional<Pessoa> findByCpf(String cpf);

    Optional<Pessoa> findByEmail(String email);

    Optional<Pessoa> findByEmailAcademico(String emailAcademico);

    Optional<Pessoa> findByMatricula(String matricula);


    /*

    @Query("SELECT new br.edu.ifpb.sgm.projeto_sgm.dto.PessoaResponseDTO("
            + "u.id, u.nome, u.email, u.matricula, u.instituicao) " // Ajuste os campos conforme o construtor do DTO
            + "FROM Pessoa u WHERE u.matricula = :matricula")
    Optional<PessoaResponseDTO> findDtoByMatricula(String matricula);
    */

}