package org.clinicavitalis.usuario.domain.repository;

import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.shared.domain.vo.Email;

import java.util.Optional;

/**
 * Port: Repositório de Usuario
 * 
 * Interface que define o contrato para persistência de usuarios.
 * Implementações (Adapters) podem usar diferentes tecnologias:
 * - Hibernate/JPA
 * - MongoDB
 * - Redis
 * etc.
 * 
 * Segue o padrão Repository e o Inversão de Controle.
 */
public interface UsuarioRepository {

    /**
     * Persiste um novo usuário no repositório.
     * 
     * @param usuario usuario a ser salvo
     * @return usuario salvo com id preenchido
     */
    Usuario salvar(Usuario usuario);

    /**
     * Encontra um usuário pelo email.
     * 
     * @param email email do usuário
     * @return Optional com o usuário caso encontrado
     */
    Optional<Usuario> encontrarPorEmail(Email email);

    /**
     * Encontra um usuário pelo ID.
     * 
     * @param id id do usuário
     * @return Optional com o usuário caso encontrado
     */
    Optional<Usuario> encontrarPorId(Long id);

    /**
     * Verifica se um email já existe no sistema.
     * 
     * @param email email a verificar
     * @return true se o email já existe
     */
    boolean existePorEmail(Email email);

    /**
     * Atualiza um usuário existente.
     * 
     * @param usuario usuario a ser atualizado
     * @return usuario atualizado
     */
    Usuario atualizar(Usuario usuario);

    /**
     * Remove um usuário (hard delete).
     * 
     * @param id id do usuário a remover
     */
    void remover(Long id);

    /**
     * Remove um usuário por email.
     * 
     * @param email email do usuário a remover
     */
    void removerPorEmail(Email email);
}
