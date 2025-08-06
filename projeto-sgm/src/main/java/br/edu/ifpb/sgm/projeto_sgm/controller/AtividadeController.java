package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.AtividadeRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AtividadeResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.AtividadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<AtividadeResponseDTO>> findAll() {
        return ResponseEntity.ok(atividadeService.findAll());
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
}