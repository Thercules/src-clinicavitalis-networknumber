package org.clinicavitalis.medico.infrastructure.persistence.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.clinicavitalis.medico.domain.entity.Medico;
import org.clinicavitalis.medico.domain.repository.MedicoRepository;
import org.clinicavitalis.medico.infrastructure.persistence.mapper.MedicoMapper;
import org.clinicavitalis.medico.infrastructure.persistence.repository.MedicoJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter (implementação do Port MedicoRepository).
 * Faz a ponte entre o domínio e a infraestrutura JPA/Panache.
 */
@ApplicationScoped
public class MedicoRepositoryAdapter implements MedicoRepository {

    @Inject
    MedicoJpaRepository jpaRepository;

    @Override
    public List<Medico> listarAtivos() {
        return jpaRepository.listarAtivosComUsuario()
                .stream()
                .map(MedicoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Medico> encontrarPorId(Long id) {
        return jpaRepository.findByIdOptional(id)
                .map(MedicoMapper::toDomain);
    }
}
