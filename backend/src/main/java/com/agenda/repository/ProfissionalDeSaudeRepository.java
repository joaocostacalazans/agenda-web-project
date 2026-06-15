package com.agenda.repository;

import com.agenda.model.ProfissionalDeSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfissionalDeSaudeRepository extends JpaRepository<ProfissionalDeSaude, Long> {

    List<ProfissionalDeSaude> findAllByOrderByNomeAsc();

    List<ProfissionalDeSaude> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    List<ProfissionalDeSaude> findByCategoriaOrderByNomeAsc(String categoria);

    List<ProfissionalDeSaude> findByNomeContainingIgnoreCaseAndCategoriaOrderByNomeAsc(String nome, String categoria);
}
