package org.clinicavitalis.medico.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.clinicavitalis.medico.application.dto.MedicoResponse;
import org.clinicavitalis.medico.domain.entity.Medico;
import org.clinicavitalis.medico.domain.exception.MedicoDomainException;
import org.clinicavitalis.medico.domain.service.MedicoDomainService;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MedicoApplicationService {

    @Inject
    MedicoDomainService medicoDomainService;

    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Fallback(fallbackMethod = "listarMedicosFallback")
    public List<MedicoResponse> listarMedicos() {
        List<Medico> medicos = medicoDomainService.listarMedicosAtivos();
        return medicos.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MedicoResponse> listarMedicosFallback() {
        throw new org.clinicavitalis.shared.domain.exception.ServicoIndisponivelException(
                "Serviço temporariamente indisponível. Tente novamente em instantes.");
    }

    private MedicoResponse mapToResponse(Medico medico) {
        MedicoResponse response = new MedicoResponse();
        response.setId(medico.getId());
        response.setUsuarioId(medico.getUsuarioId());
        response.setNomeCompleto(medico.getNomeCompleto());
        response.setCrmNumero(medico.getCrmNumero());
        response.setCrmEstado(medico.getCrmEstado());
        response.setCrmSituacao(medico.getCrmSituacao() != null ? medico.getCrmSituacao().name() : null);
        response.setCrmDataEmissao(medico.getCrmDataEmissao());
        response.setEmailProfissional(medico.getEmailProfissional());
        response.setEspecialidades(medico.getEspecialidades());
        response.setFotoUrl(medico.getFotoUrl());
        response.setLocalizacao(medico.getLocalizacao());
        response.setDiasAtendimento(medico.getDiasAtendimento());
        response.setHorarioInicioManha(medico.getHorarioInicioManha());
        response.setHorarioFimManha(medico.getHorarioFimManha());
        response.setHorarioInicioTarde(medico.getHorarioInicioTarde());
        response.setHorarioFimTarde(medico.getHorarioFimTarde());
        response.setIntervaloConsultaMinutos(medico.getIntervaloConsultaMinutos());
        response.setAtivo(medico.getAtivo());

        LocalDate dataLimite = LocalDate.now()
                .plusDays((long) medico.getSemanasDisponibilidade() * 7);
        response.setDataLimiteAgendamento(dataLimite);

        return response;
    }
}
