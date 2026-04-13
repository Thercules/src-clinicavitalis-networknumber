package org.clinicavitalis.usuario.infrastructure.persistence.adapter;

import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.usuario.domain.repository.UsuarioRepository;
import org.clinicavitalis.usuario.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.clinicavitalis.usuario.infrastructure.persistence.mapper.UsuarioMapper;
import org.clinicavitalis.usuario.infrastructure.persistence.repository.UsuarioJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    @Inject
    UsuarioJpaRepository jpaRepository;

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioJpaEntity entity = UsuarioMapper.toJpaEntity(usuario);
        jpaRepository.persist(entity);
        jpaRepository.flush();

        return UsuarioMapper.toDomain(entity);
    }

    @Override
    public Optional<Usuario> encontrarPorEmail(Email email) {
        UsuarioJpaEntity entity = jpaRepository.encontrarPorEmail(email.getValue());
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(UsuarioMapper.toDomain(entity));
    }

    @Override
    public Optional<Usuario> encontrarPorId(Long id) {
        Optional<UsuarioJpaEntity> entity = jpaRepository.findByIdOptional(id);
        return entity.map(UsuarioMapper::toDomain);
    }

    @Override
    public boolean existePorEmail(Email email) {
        return jpaRepository.existePorEmail(email.getValue());
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        UsuarioJpaEntity entity = UsuarioMapper.toJpaEntity(usuario);
        UsuarioJpaEntity updated = jpaRepository.getEntityManager().merge(entity);
        jpaRepository.flush();
        return UsuarioMapper.toDomain(updated);
    }
}
