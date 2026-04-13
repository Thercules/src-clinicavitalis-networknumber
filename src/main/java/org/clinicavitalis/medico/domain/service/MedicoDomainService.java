package org.clinicavitalis.medico.domain.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.clinicavitalis.medico.domain.entity.Medico;
import org.clinicavitalis.medico.domain.repository.MedicoRepository;

import java.util.List;

@ApplicationScoped
public class MedicoDomainService implements MedicoService {

    @Inject
    MedicoRepository medicoRepository;

    public List<Medico> listarMedicosAtivos() {
        return medicoRepository.listarAtivos();
    }
}
