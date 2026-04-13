package org.clinicavitalis.usuario.infrastructure.http;

import org.clinicavitalis.shared.application.dto.ApiResponse;
import org.clinicavitalis.shared.domain.exception.ServicoIndisponivelException;
import org.clinicavitalis.usuario.application.dto.AuthTokenResponse;
import org.clinicavitalis.usuario.application.dto.RefreshTokenRequest;
import org.clinicavitalis.usuario.application.dto.RefreshTokenResponse;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;
import org.clinicavitalis.usuario.application.dto.CreateUserRequest;
import org.clinicavitalis.usuario.application.dto.LoginRequest;
import org.clinicavitalis.usuario.application.dto.RegisterRequest;
import org.clinicavitalis.usuario.application.dto.UsuarioResponse;
import org.clinicavitalis.usuario.application.service.AutenticacaoApplicationService;
import org.clinicavitalis.usuario.domain.port.TokenProvider;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AutenticacaoResource {

    @Inject
    AutenticacaoApplicationService autenticacaoService;

    @Inject
    TokenProvider tokenProvider;

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        try {
            AuthTokenResponse resultado = autenticacaoService.login(request);

            ApiResponse<AuthTokenResponse> response = ApiResponse.success(
                "Login realizado com sucesso",
                resultado
            );

            return Response
                .ok(response)
                .status(Response.Status.OK)
                .build();

        } catch (org.clinicavitalis.usuario.domain.exception.CredenciaisInvalidasException e) {
            ApiResponse<Object> response = ApiResponse.error("Email ou senha incorretos");
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(response)
                .build();

        } catch (CircuitBreakerOpenException | ServicoIndisponivelException e) {
            ApiResponse<Object> response = ApiResponse.error(
                "Serviço temporariamente indisponível. Tente novamente em instantes.");
            return Response
                .status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(response)
                .build();

        } catch (Exception e) {
            ApiResponse<Object> response = ApiResponse.error(
                "Erro ao fazer login: " + e.getMessage()
            );
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
        }
    }

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        try {
            AuthTokenResponse resultado = autenticacaoService.registrar(request);

            ApiResponse<AuthTokenResponse> response = ApiResponse.success(
                "Usuário criado com sucesso",
                resultado
            );

            return Response
                .status(Response.Status.CREATED)
                .entity(response)
                .build();

        } catch (org.clinicavitalis.usuario.domain.exception.EmailAlreadyExistsException e) {
            ApiResponse<Object> response = ApiResponse.error(e.getMessage());
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();

        } catch (IllegalArgumentException e) {
            ApiResponse<Object> response = ApiResponse.error(e.getMessage());
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();

        } catch (CircuitBreakerOpenException | ServicoIndisponivelException e) {
            ApiResponse<Object> response = ApiResponse.error(
                "Serviço temporariamente indisponível. Tente novamente em instantes.");
            return Response
                .status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(response)
                .build();

        } catch (Exception e) {
            ApiResponse<Object> response = ApiResponse.error(
                "Erro ao registrar usuário: " + e.getMessage()
            );
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
        }
    }

    @POST
    @Path("/refresh-token")
    public Response refreshToken(RefreshTokenRequest request) {
        try {
            String novoToken = autenticacaoService.renovarToken(request.getRefreshToken());

            RefreshTokenResponse data = new RefreshTokenResponse(
                novoToken,
                request.getRefreshToken()
            );

            ApiResponse<RefreshTokenResponse> response = ApiResponse.success(
                "Token renovado com sucesso",
                data
            );

            return Response
                .ok(response)
                .build();

        } catch (org.clinicavitalis.usuario.domain.exception.UsuarioDomainException e) {
            ApiResponse<Object> response = ApiResponse.error(e.getMessage());
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(response)
                .build();

        } catch (CircuitBreakerOpenException | ServicoIndisponivelException e) {
            ApiResponse<Object> response = ApiResponse.error(
                "Serviço temporariamente indisponível. Tente novamente em instantes.");
            return Response
                .status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(response)
                .build();

        } catch (Exception e) {
            ApiResponse<Object> response = ApiResponse.error(
                "Erro ao renovar token: " + e.getMessage()
            );
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
        }
    }

    @POST
    @Path("/register-with-access-level")
    public Response registerWithAccessLevel(
        @Valid CreateUserRequest request,
        @HeaderParam("Authorization") String authHeader
    ) {
        try {

            if (authHeader == null || authHeader.isEmpty()) {
                ApiResponse<Object> response = ApiResponse.error("Token não fornecido");
                return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(response)
                    .build();
            }

            String token = authHeader.replace("Bearer ", "").trim();

            if (!tokenProvider.isTokenValid(token)) {
                ApiResponse<Object> response = ApiResponse.error("Token inválido ou expirado");
                return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(response)
                    .build();
            }

            Long usuarioId = tokenProvider.getIdFromToken(token);
            String nivelDoUsuario = tokenProvider.getNivelFromToken(token);

            UsuarioResponse usuarioCriado = autenticacaoService.criarUsuarioComNivel(
                request,
                usuarioId,
                nivelDoUsuario
            );

            ApiResponse<UsuarioResponse> response = ApiResponse.success(
                "Usuário criado com sucesso",
                usuarioCriado
            );

            return Response
                .status(Response.Status.CREATED)
                .entity(response)
                .build();

        } catch (org.clinicavitalis.usuario.domain.exception.EmailAlreadyExistsException e) {
            ApiResponse<Object> response = ApiResponse.error(e.getMessage());
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();

        } catch (org.clinicavitalis.usuario.domain.exception.UsuarioDomainException e) {

            ApiResponse<Object> response = ApiResponse.error(e.getMessage());
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity(response)
                .build();

        } catch (IllegalArgumentException e) {
            ApiResponse<Object> response = ApiResponse.error(e.getMessage());
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();

        } catch (CircuitBreakerOpenException | ServicoIndisponivelException e) {
            ApiResponse<Object> response = ApiResponse.error(
                "Serviço temporariamente indisponível. Tente novamente em instantes.");
            return Response
                .status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(response)
                .build();

        } catch (Exception e) {
            ApiResponse<Object> response = ApiResponse.error(
                "Erro ao criar usuário: " + e.getMessage()
            );
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
        }
    }
}
