package org.clinicavitalis.medico.infrastructure.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.clinicavitalis.medico.infrastructure.persistence.entity.MedicoJpaEntity;

import java.util.List;

/**
 * Panache Repository para MedicoJpaEntity.
 * A query usa JOIN FETCH para carregar o relacionamento com usuarios em uma única consulta,
 * evitando N+1 e garantindo que nome_completo esteja disponível no mapper.
 */
@ApplicationScoped
public class MedicoJpaRepository implements PanacheRepository<MedicoJpaEntity> {

    public List<MedicoJpaEntity> listarAtivosComUsuario() {
        return getEntityManager()
                .createQuery(
                        "SELECT m FROM MedicoJpaEntity m " +
                        "JOIN FETCH m.usuario u " +
                        "WHERE m.ativo = true AND u.ativo = true",
                        MedicoJpaEntity.class)
                .getResultList();
    }
}
