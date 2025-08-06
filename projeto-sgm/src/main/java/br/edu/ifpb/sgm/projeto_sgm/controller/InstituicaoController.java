package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.InstituicaoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.InstituicaoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.InstituicaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instituicoes")
public class InstituicaoController {

    private final InstituicaoService instituicaoService;

    public InstituicaoController(InstituicaoService instituicaoService) {
        this.instituicaoService = instituicaoService;
    }

    @PostMapping
    public ResponseEntity<InstituicaoResponseDTO> create(@RequestBody InstituicaoRequestDTO dto) {
        InstituicaoResponseDTO savedInstituicao = instituicaoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInstituicao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(instituicaoService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<InstituicaoResponseDTO>> findAll() {
        return ResponseEntity.ok(instituicaoService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstituicaoResponseDTO> update(@PathVariable Long id, @RequestBody InstituicaoRequestDTO dto) {
        return ResponseEntity.ok(instituicaoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        instituicaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}