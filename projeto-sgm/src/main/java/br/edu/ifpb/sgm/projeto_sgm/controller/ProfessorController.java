package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> create(@RequestBody ProfessorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> findAll() {
        return ResponseEntity.ok(professorService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> update(@PathVariable Long id, @RequestBody ProfessorRequestDTO dto) {
        return ResponseEntity.ok(professorService.update(id, dto));
    }

    @PutMapping("/associar/{pessoaId}")
    public ResponseEntity<ProfessorResponseDTO> associate(@PathVariable Long pessoaId, @RequestBody ProfessorRequestDTO dto) {
        return ResponseEntity.ok(professorService.associate(pessoaId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        professorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}