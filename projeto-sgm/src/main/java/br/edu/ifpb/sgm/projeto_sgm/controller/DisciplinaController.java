package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.DisciplinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponseDTO> create(@RequestBody DisciplinaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(disciplinaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaResponseDTO>> findAll() {
        return ResponseEntity.ok(disciplinaService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaResponseDTO> update(@PathVariable Long id, @RequestBody DisciplinaRequestDTO dto) {
        return ResponseEntity.ok(disciplinaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        disciplinaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}