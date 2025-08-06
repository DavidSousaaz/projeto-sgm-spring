package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.ProcessoSeletivoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processos-seletivos")
public class ProcessoSeletivoController {

    private final ProcessoSeletivoService processoSeletivoService;

    public ProcessoSeletivoController(ProcessoSeletivoService processoSeletivoService) {
        this.processoSeletivoService = processoSeletivoService;
    }

    @PostMapping
    public ResponseEntity<ProcessoSeletivoResponseDTO> create(@RequestBody ProcessoSeletivoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(processoSeletivoService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessoSeletivoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(processoSeletivoService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProcessoSeletivoResponseDTO>> findAll() {
        return ResponseEntity.ok(processoSeletivoService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcessoSeletivoResponseDTO> update(@PathVariable Long id, @RequestBody ProcessoSeletivoRequestDTO dto) {
        return ResponseEntity.ok(processoSeletivoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        processoSeletivoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}