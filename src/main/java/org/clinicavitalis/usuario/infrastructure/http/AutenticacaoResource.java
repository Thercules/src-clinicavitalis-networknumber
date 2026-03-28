package org.clinicavitalis.usuario.infrastructure.http;

import org.clinicavitalis.shared.application.dto.ApiResponse;
import org.clinicavitalis.usuario.application.dto.AuthTokenResponse;
import org.clinicavitalis.usuario.application.dto.LoginRequest;
import org.clinicavitalis.usuario.application.dto.RegisterRequest;
import org.clinicavitalis.usuario.application.service.AutenticacaoApplicationService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST Resource: Autenticação
 * 
 * Adapter de entrada que implementa o contrato HTTP para autenticação.
 * 
 * Endpoints:
 * - POST /api/auth/login
 * - POST /api/auth/register
 * - POST /api/auth/refresh-token
 * 
 * Segue o padrão de resposta definido em DATABASE_AUTH_DESIGN.md
 */
@Path("/api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AutenticacaoResource {

    @Inject
    AutenticacaoApplicationService autenticacaoService;

    /**
     * Endpoint: POST /api/auth/login
     * 
     * Realiza login de um usuário existente.
     * 
     * Request Body:
     * {
     *   "email": "usuario@example.com",
     *   "password": "senha123"
     * }
     * 
     * Response (200 OK):
     * {
     *   "sucesso": true,
     *   "mensagem": "Login realizado com sucesso",
     *   "dados": {
     *     "token": "eyJhbGciOiJIUzI1NiIs...",
     *     "refresh_token": "...",
     *     "usuario": { ... }
     *   }
     * }
     * 
     * Response (401 Unauthorized):
     * {
     *   "sucesso": false,
     *   "mensagem": "Email ou senha incorretos"
     * }
     */
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

    /**
     * Endpoint: POST /api/auth/register
     * 
     * Registra um novo usuário no sistema.
     * 
     * Request Body:
     * {
     *   "email": "novo@example.com",
     *   "password": "senha123",
     *   "confirmPassword": "senha123",
     *   "nome_completo": "Maria Santos",
     *   "telefone": "(11) 99999-9999"
     * }
     * 
     * Response (201 Created):
     * {
     *   "sucesso": true,
     *   "mensagem": "Usuário criado com sucesso",
     *   "dados": {
     *     "token": "eyJhbGciOiJIUzI1NiIs...",
     *     "refresh_token": "...",
     *     "usuario": { ... }
     *   }
     * }
     * 
     * Response (400 Bad Request):
     * {
     *   "sucesso": false,
     *   "mensagem": "Email já existente no sistema"
     * }
     */
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

    /**
     * Endpoint: POST /api/auth/refresh-token
     * 
     * Renova o access token usando um refresh token válido.
     * 
     * Request Body:
     * {
     *   "refresh_token": "abc123def456..."
     * }
     * 
     * Response (200 OK):
     * {
     *   "sucesso": true,
     *   "dados": {
     *     "token": "novo_token_aqui...",
     *     "refresh_token": "novo_refresh_token..."
     *   }
     * }
     */
    @POST
    @Path("/refresh-token")
    public Response refreshToken(RefreshTokenRequest request) {
        try {
            String novoToken = autenticacaoService.renovarToken(request.getRefresh_token());

            // Gera novo refresh token também
            RefreshTokenResponse data = new RefreshTokenResponse(
                novoToken,
                request.getRefresh_token() // Na prática, geraria um novo
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

    // ========== Inner DTOs para este Resource ==========

    /**
     * DTO para requisição de refresh token.
     */
    public static class RefreshTokenRequest {
        private String refresh_token;

        public RefreshTokenRequest() {
        }

        public RefreshTokenRequest(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }
    }

    /**
     * DTO para resposta de refresh token.
     */
    public static class RefreshTokenResponse {
        private String token;
        private String refresh_token;

        public RefreshTokenResponse() {
        }

        public RefreshTokenResponse(String token, String refresh_token) {
            this.token = token;
            this.refresh_token = refresh_token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }
    }
}
