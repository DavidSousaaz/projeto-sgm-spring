package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.CursoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.CursoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<CursoResponseDTO> create(@RequestBody CursoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> findAll() {
        return ResponseEntity.ok(cursoService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> update(@PathVariable Long id, @RequestBody CursoRequestDTO dto) {
        return ResponseEntity.ok(cursoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}