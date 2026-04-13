package org.clinicavitalis.usuario.infrastructure.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.clinicavitalis.usuario.infrastructure.persistence.entity.UsuarioJpaEntity;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Spring Data JPA Repository para Usuario.
 * Fornece operações básicas de CRUD via Panache.
 */
@ApplicationScoped
public class UsuarioJpaRepository implements PanacheRepository<UsuarioJpaEntity> {

    /**
     * Encontra um usuário pelo email.
     * 
     * @param email email a buscar
     * @return UsuarioJpaEntity ou null
     */
    public UsuarioJpaEntity encontrarPorEmail(String email) {
        return find("email", email.toLowerCase()).firstResult();
    }

    /**
     * Verifica se um email já existe.
     * 
     * @param email email a verificar
     * @return true se existe
     */
    public boolean existePorEmail(String email) {
        return find("email", email.toLowerCase()).count() > 0;
    }
}
