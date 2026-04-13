package org.clinicavitalis.medico.domain.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.clinicavitalis.medico.domain.entity.Medico;
import org.clinicavitalis.medico.domain.repository.MedicoRepository;

import java.util.List;

/**
 * Domain Service: regras de negócio relacionadas ao Medico
 * que não pertencem a nenhuma entidade específica.
 */
@ApplicationScoped
public class MedicoDomainService {

    @Inject
    MedicoRepository medicoRepository;

    public List<Medico> listarMedicosAtivos() {
        return medicoRepository.listarAtivos();
    }
}
