package org.clinicavitalis.usuario.infrastructure.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.clinicavitalis.usuario.infrastructure.persistence.entity.UsuarioJpaEntity;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioJpaRepository implements PanacheRepository<UsuarioJpaEntity> {

    public UsuarioJpaEntity encontrarPorEmail(String email) {
        return find("email", email.toLowerCase()).firstResult();
    }

    public boolean existePorEmail(String email) {
        return find("email", email.toLowerCase()).count() > 0;
    }
}
