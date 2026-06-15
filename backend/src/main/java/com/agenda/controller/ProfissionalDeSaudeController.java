package com.agenda.controller;

import com.agenda.model.ProfissionalDeSaude;
import com.agenda.repository.ProfissionalDeSaudeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
public class ProfissionalDeSaudeController {

    private final ProfissionalDeSaudeRepository repository;

    public ProfissionalDeSaudeController(ProfissionalDeSaudeRepository repository) {
        this.repository = repository;
    }

    // CREATE - Inserir
    @PostMapping
    public ResponseEntity<ProfissionalDeSaude> criar(@Valid @RequestBody ProfissionalDeSaude profissional) {
        ProfissionalDeSaude salvo = repository.save(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // READ - Listar/Consultar
    @GetMapping
    public ResponseEntity<List<ProfissionalDeSaude>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria) {
        
        List<ProfissionalDeSaude> profissionais;
        
        if (nome != null && !nome.isBlank() && categoria != null && !categoria.isBlank()) {
            profissionais = repository.findByNomeContainingIgnoreCaseAndCategoriaOrderByNomeAsc(nome, categoria);
        } else if (nome != null && !nome.isBlank()) {
            profissionais = repository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome);
        } else if (categoria != null && !categoria.isBlank()) {
            profissionais = repository.findByCategoriaOrderByNomeAsc(categoria);
        } else {
            profissionais = repository.findAllByOrderByNomeAsc();
        }
        
        return ResponseEntity.ok(profissionais);
    }

    // READ - Consultar por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalDeSaude> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE - Alterar (id)
    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalDeSaude> atualizar(@PathVariable Long id,
                                                         @Valid @RequestBody ProfissionalDeSaude dados) {
        return repository.findById(id)
                .map(profissional -> {
                    profissional.setNome(dados.getNome());
                    profissional.setTelefone(dados.getTelefone());
                    profissional.setEndereco(dados.getEndereco());
                    profissional.setCategoria(dados.getCategoria());
                    return ResponseEntity.ok(repository.save(profissional));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Excluir (id)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(profissional -> {
                    repository.delete(profissional);
                    return ResponseEntity.ok(Map.of("mensagem", "Profissional de saúde removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
