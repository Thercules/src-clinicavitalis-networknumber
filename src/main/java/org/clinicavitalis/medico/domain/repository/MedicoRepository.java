package org.clinicavitalis.medico.domain.repository;

import org.clinicavitalis.medico.domain.entity.Medico;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository {

    List<Medico> listarAtivos();

    Optional<Medico> encontrarPorId(Long id);
}
