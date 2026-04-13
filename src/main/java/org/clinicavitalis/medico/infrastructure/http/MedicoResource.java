package org.clinicavitalis.medico.infrastructure.http;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.clinicavitalis.medico.application.dto.MedicoResponse;
import org.clinicavitalis.medico.application.service.MedicoApplicationService;
import org.clinicavitalis.medico.domain.exception.MedicoDomainException;
import org.clinicavitalis.shared.application.dto.ApiResponse;
import org.clinicavitalis.shared.domain.exception.ServicoIndisponivelException;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;

import java.util.List;

@Path("/api/medicos")
@Produces(MediaType.APPLICATION_JSON)
public class MedicoResource {

    @Inject
    MedicoApplicationService medicoApplicationService;

    @GET
    public Response listarMedicos() {
        try {
            List<MedicoResponse> medicos = medicoApplicationService.listarMedicos();

            ApiResponse<List<MedicoResponse>> response = ApiResponse.success(
                    "Médicos listados com sucesso",
                    medicos
            );

            return Response.ok(response).build();

        } catch (CircuitBreakerOpenException | ServicoIndisponivelException e) {
            ApiResponse<Object> response = ApiResponse.error(
                    "Serviço temporariamente indisponível. Tente novamente em instantes.");
            return Response
                    .status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(response)
                    .build();

        } catch (MedicoDomainException e) {
            ApiResponse<Object> response = ApiResponse.error(e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();

        } catch (Exception e) {
            ApiResponse<Object> response = ApiResponse.error(
                    "Erro ao listar médicos: " + e.getMessage()
            );
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }
}
