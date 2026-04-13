package org.clinicavitalis.medico.domain.service;

import org.clinicavitalis.medico.domain.entity.Medico;

import java.util.List;

public interface MedicoService {

    List<Medico> listarMedicosAtivos();
}
