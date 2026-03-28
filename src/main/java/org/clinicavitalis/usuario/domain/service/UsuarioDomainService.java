package org.clinicavitalis.usuario.domain.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.usuario.domain.exception.EmailAlreadyExistsException;
import org.clinicavitalis.usuario.domain.repository.UsuarioRepository;

/**
 * Domain Service: Serviço de Domínio para Usuario
 * 
 * Encapsula lógica complexa de negócio relacionada a usuários.
 * Diferencia-se da Application Service por:
 * - Ser orientado a Domínio (usa entidades e value objects)
 * - Não tem dependência de frameworks
 * - Realiza validações de regras de negócio
 */
@ApplicationScoped
public class UsuarioDomainService {

    @Inject
    UsuarioRepository usuarioRepository;

    /**
     * Verifica se um email já existe e lança exceção se existir.
     * Esta é uma regra de negócio importante: unicidade de email.
     * 
     * @param email email a verificar
     * @throws EmailAlreadyExistsException se o email já existe
     */
    public void validarEmailUnico(Email email) {
        if (usuarioRepository.existePorEmail(email)) {
            throw new EmailAlreadyExistsException(email.getValue());
        }
    }

    /**
     * Registra um novo usuário no sistema.
     * Controla o fluxo de criação validando regras de negócio.
     * 
     * @param usuario usuario a ser registrado
     * @return usuario registrado
     * @throws EmailAlreadyExistsException se o email já existe
     */
    public Usuario registrarNovoUsuario(Usuario usuario) {
        validarEmailUnico(usuario.getEmail());
        return usuarioRepository.salvar(usuario);
    }

    /**
     * Encontra um usuário pelo email.
     * 
     * @param email email do usuário
     * @return usuario encontrado
     * @throws org.clinicavitalis.shared.domain.exception.EntityNotFoundException se não encontrado
     */
    public Usuario encontrarPorEmail(Email email) {
        return usuarioRepository
            .encontrarPorEmail(email)
            .orElseThrow(() -> 
                new org.clinicavitalis.shared.domain.exception.EntityNotFoundException(
                    "Usuario", email.getValue()
                )
            );
    }

    /**
     * Encontra um usuário pelo ID.
     * 
     * @param id id do usuário
     * @return usuario encontrado
     * @throws org.clinicavitalis.shared.domain.exception.EntityNotFoundException se não encontrado
     */
    public Usuario encontrarPorId(Long id) {
        return usuarioRepository
            .encontrarPorId(id)
            .orElseThrow(() -> 
                new org.clinicavitalis.shared.domain.exception.EntityNotFoundException(
                    "Usuario", id
                )
            );
    }
}
