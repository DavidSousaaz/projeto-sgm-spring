package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> create(@RequestBody AlunoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> findAll() {
        return ResponseEntity.ok(alunoService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> update(@PathVariable Long id, @RequestBody AlunoRequestDTO dto) {
        return ResponseEntity.ok(alunoService.update(id, dto));
    }

    @PutMapping("/associar/{pessoaId}")
    public ResponseEntity<AlunoResponseDTO> associate(@PathVariable Long pessoaId, @RequestBody AlunoRequestDTO dto) {
        return ResponseEntity.ok(alunoService.associate(pessoaId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}