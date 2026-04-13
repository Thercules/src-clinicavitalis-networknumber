package org.clinicavitalis.medico.domain.repository;

import org.clinicavitalis.medico.domain.entity.Medico;

import java.util.List;
import java.util.Optional;

/**
 * Port (interface) do repositório de Medico.
 * Definido no domínio — implementado na camada de infraestrutura.
 */
public interface MedicoRepository {

    List<Medico> listarAtivos();

    Optional<Medico> encontrarPorId(Long id);
}
