package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.PessoaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.PessoaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.PessoaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController extends RestAppController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<PessoaResponseDTO> create(@RequestBody PessoaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pessoaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponseDTO>> findAll() {
        return ResponseEntity.ok(pessoaService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> update(@PathVariable Long id, @RequestBody PessoaRequestDTO dto) {
        return ResponseEntity.ok(pessoaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}