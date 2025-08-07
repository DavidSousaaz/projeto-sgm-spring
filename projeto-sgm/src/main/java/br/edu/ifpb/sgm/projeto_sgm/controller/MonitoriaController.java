package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.*;
import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;
import br.edu.ifpb.sgm.projeto_sgm.service.MonitoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitorias")
public class MonitoriaController {

    private final MonitoriaService monitoriaService;

    public MonitoriaController(MonitoriaService monitoriaService) {
        this.monitoriaService = monitoriaService;
    }

    @PostMapping
    public ResponseEntity<MonitoriaResponseDTO> create(@RequestBody MonitoriaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(monitoriaService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitoriaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(monitoriaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<MonitoriaResponseDTO>> findAll(
            @RequestParam(name = "processoId", required = false) Long processoId
    ) {
        return ResponseEntity.ok(monitoriaService.findAll(processoId));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<DashboardMonitoriaDTO>> getDashboard() {
        return ResponseEntity.ok(monitoriaService.getDashboardData());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonitoriaResponseDTO> update(@PathVariable Long id, @RequestBody MonitoriaRequestDTO dto) {
        return ResponseEntity.ok(monitoriaService.update(id, dto));
    }

    @PutMapping("/{monitoriaId}/inscricoes/{alunoId}/selecionar")
    public ResponseEntity<MonitoriaInscritosResponseDTO> selecionarMonitor(
            @PathVariable Long monitoriaId,
            @PathVariable Long alunoId
    ) {
        MonitoriaInscritosResponseDTO inscricao = monitoriaService.selecionarMonitor(monitoriaId, alunoId);
        return ResponseEntity.ok(inscricao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        monitoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/inscricoes")
    public ResponseEntity<MonitoriaInscritosResponseDTO> inscrever(
            @PathVariable Long id,
            @AuthenticationPrincipal Pessoa pessoa,
            @RequestBody InscricaoRequestDTO inscricaoDTO
    ) {
        MonitoriaInscritosResponseDTO inscricao = monitoriaService.realizarInscricao(id, pessoa, inscricaoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(inscricao);
    }
}