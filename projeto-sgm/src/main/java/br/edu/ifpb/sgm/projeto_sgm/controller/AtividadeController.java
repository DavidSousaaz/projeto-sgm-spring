package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.AtividadeRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AtividadeResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AtualizacaoStatusDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;
import br.edu.ifpb.sgm.projeto_sgm.service.AtividadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atividades")
public class AtividadeController {

    private final AtividadeService atividadeService;

    public AtividadeController(AtividadeService atividadeService) {
        this.atividadeService = atividadeService;
    }

    @PostMapping
    public ResponseEntity<AtividadeResponseDTO> create(@RequestBody AtividadeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atividadeService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtividadeResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(atividadeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AtividadeResponseDTO>> findAll(
            @RequestParam(name = "monitoriaId", required = false) Long monitoriaId
    ) {
        return ResponseEntity.ok(atividadeService.findAll(monitoriaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtividadeResponseDTO> update(@PathVariable Long id, @RequestBody AtividadeRequestDTO dto) {
        return ResponseEntity.ok(atividadeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        atividadeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AtividadeResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizacaoStatusDTO statusDTO,
            @AuthenticationPrincipal Pessoa pessoaLogada
    ) {
        return ResponseEntity.ok(atividadeService.atualizarStatus(id, statusDTO, pessoaLogada));
    }

}