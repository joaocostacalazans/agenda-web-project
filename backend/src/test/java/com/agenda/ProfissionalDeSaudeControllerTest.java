package com.agenda;

import com.agenda.controller.ProfissionalDeSaudeController;
import com.agenda.model.ProfissionalDeSaude;
import com.agenda.repository.ProfissionalDeSaudeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfissionalDeSaudeController.class)
public class ProfissionalDeSaudeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfissionalDeSaudeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfissionalDeSaude p1;
    private ProfissionalDeSaude p2;

    @BeforeEach
    void setUp() {
        p1 = new ProfissionalDeSaude(1L, "João Silva", "11999999999", "Rua A, 123", "Médico", null);
        p2 = new ProfissionalDeSaude(2L, "Maria Souza", "11888888888", "Avenida B, 456", "Psicólogo", null);
    }

    @Test
    void deveCriarProfissionalComSucesso() throws Exception {
        Mockito.when(repository.save(any(ProfissionalDeSaude.class))).thenReturn(p1);

        mockMvc.perform(post("/api/profissionais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("João Silva")))
                .andExpect(jsonPath("$.categoria", is("Médico")));
    }

    @Test
    void deveListarTodosOsProfissionais() throws Exception {
        Mockito.when(repository.findAllByOrderByNomeAsc()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/profissionais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is("João Silva")))
                .andExpect(jsonPath("$[1].nome", is("Maria Souza")));
    }

    @Test
    void deveBuscarProfissionalPorId() throws Exception {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(p1));

        mockMvc.perform(get("/api/profissionais/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("João Silva")));
    }

    @Test
    void deveRetornarNotFoundAoBuscarIdInexistente() throws Exception {
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profissionais/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarProfissionalComSucesso() throws Exception {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(p1));
        Mockito.when(repository.save(any(ProfissionalDeSaude.class))).thenReturn(p1);

        mockMvc.perform(put("/api/profissionais/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("João Silva")));
    }

    @Test
    void deveDeletarProfissionalComSucesso() throws Exception {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(p1));

        mockMvc.perform(delete("/api/profissionais/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem", is("Profissional de saúde removido com sucesso")));
    }
}
