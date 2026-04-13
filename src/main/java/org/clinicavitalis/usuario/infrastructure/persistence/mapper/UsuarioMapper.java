package org.clinicavitalis.usuario.infrastructure.persistence.mapper;

import org.clinicavitalis.shared.domain.vo.CPF;
import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.shared.domain.vo.Telefone;
import org.clinicavitalis.usuario.domain.entity.NivelDeAcesso;
import org.clinicavitalis.usuario.domain.entity.Usuario;
import org.clinicavitalis.usuario.infrastructure.persistence.entity.UsuarioJpaEntity;

public class UsuarioMapper {

    private UsuarioMapper() {

    }

    public static UsuarioJpaEntity toJpaEntity(Usuario usuario) {
        UsuarioJpaEntity entity = new UsuarioJpaEntity();

        if (usuario.getId() != null) {
            entity.setId(usuario.getId());
        }

        entity.setEmail(usuario.getEmail().getValue());
        entity.setSenha(usuario.getSenha());
        entity.setNomeCompleto(usuario.getNomeCompleto());
        entity.setNivelDeAcesso(UsuarioJpaEntity.UsuarioNivelDeAcesso.valueOf(
            usuario.getNivelDeAcesso().name()
        ));
        entity.setDataCriacao(usuario.getDataCriacao());
        entity.setDataAtualizacao(usuario.getDataAtualizacao());
        entity.setAtivo(usuario.getAtivo());
        entity.setUltimoAcesso(usuario.getUltimoAcesso());

        if (usuario.getTelefone() != null) {
            entity.setTelefone(usuario.getTelefone().getValue());
        }

        if (usuario.getCpf() != null) {
            entity.setCpf(usuario.getCpf().getValue());
        }

        entity.setResetaSenhaToken(usuario.getResetaSenhaToken());
        entity.setResetaSenhaExpiracao(usuario.getResetaSenhaExpiracao());
        entity.setEmailVerificado(usuario.getEmailVerificado());
        entity.setVerificacaoEmailToken(usuario.getVerificacaoEmailToken());
        entity.setCriadoPor(usuario.getCriadoPor());

        return entity;
    }

    public static Usuario toDomain(UsuarioJpaEntity entity) {
        Email email = Email.of(entity.getEmail());

        CPF cpf = null;
        if (entity.getCpf() != null && !entity.getCpf().isEmpty()) {
            try {
                cpf = CPF.of(entity.getCpf());
            } catch (IllegalArgumentException e) {

            }
        }

        Telefone telefone = null;
        if (entity.getTelefone() != null && !entity.getTelefone().isEmpty()) {
            try {
                telefone = Telefone.of(entity.getTelefone());
            } catch (IllegalArgumentException e) {

            }
        }

        NivelDeAcesso nivelDeAcesso = NivelDeAcesso.valueOf(
            entity.getNivelDeAcesso().name()
        );

        return Usuario.reconectar(
            entity.getId(),
            email,
            entity.getSenha(),
            entity.getNomeCompleto(),
            nivelDeAcesso,
            entity.getDataCriacao(),
            entity.getDataAtualizacao(),
            entity.getAtivo(),
            entity.getUltimoAcesso(),
            telefone,
            cpf,
            entity.getResetaSenhaToken(),
            entity.getResetaSenhaExpiracao(),
            entity.getEmailVerificado(),
            entity.getVerificacaoEmailToken(),
            entity.getCriadoPor()
        );
    }
}
