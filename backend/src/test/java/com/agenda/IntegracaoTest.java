package com.agenda;

import com.agenda.model.Atendimento;
import com.agenda.model.ProfissionalDeSaude;
import com.agenda.repository.AtendimentoRepository;
import com.agenda.repository.ProfissionalDeSaudeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class IntegracaoTest {

    @Autowired
    private ProfissionalDeSaudeRepository profissionalRepository;

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Test
    void deveSalvarEBuscarAtendimentoVinculadoAProfissional() {
        // 1. Criar e salvar profissional de saúde
        ProfissionalDeSaude prof = new ProfissionalDeSaude();
        prof.setNome("Dr. Ricardo Oliveira");
        prof.setTelefone("11999998888");
        prof.setEndereco("Av Paulista, 1000");
        prof.setCategoria("Médico");
        ProfissionalDeSaude profSalvo = profissionalRepository.save(prof);

        assertNotNull(profSalvo.getId());

        // 2. Criar e salvar atendimento vinculado
        Atendimento atendimento = new Atendimento();
        atendimento.setData(LocalDate.now());
        atendimento.setHorario(LocalTime.of(15, 0));
        atendimento.setProblemaTexto("Consulta de rotina");
        atendimento.setReceitaSaude(Arrays.asList("Remédio A de 8h em 8h"));
        atendimento.setProfissional(profSalvo);
        Atendimento atendimentoSalvo = atendimentoRepository.save(atendimento);

        assertNotNull(atendimentoSalvo.getId());
        assertEquals(profSalvo.getId(), atendimentoSalvo.getProfissional().getId());

        // 3. Buscar atendimentos por profissional
        List<Atendimento> atendimentosDoProf = atendimentoRepository.findByProfissionalIdOrderByDataAscHorarioAsc(profSalvo.getId());
        assertFalse(atendimentosDoProf.isEmpty());
        assertEquals("Consulta de rotina", atendimentosDoProf.get(0).getProblemaTexto());

        // Limpar dados
        atendimentoRepository.delete(atendimentoSalvo);
        profissionalRepository.delete(profSalvo);
    }
}
