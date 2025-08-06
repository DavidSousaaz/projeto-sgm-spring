package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {

    // Optional<Instituicao> findByCnpj(String cnpj);
}