package org.clinicavitalis.medico.infrastructure.persistence.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.clinicavitalis.medico.domain.entity.CrmSituacao;
import org.clinicavitalis.medico.domain.entity.Medico;
import org.clinicavitalis.medico.domain.entity.Sexo;
import org.clinicavitalis.medico.infrastructure.persistence.entity.MedicoJpaEntity;
import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.shared.domain.vo.Endereco;

import java.util.ArrayList;
import java.util.List;

public class MedicoMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private MedicoMapper() {}

    public static Medico toDomain(MedicoJpaEntity entity) {
        String nomeCompleto = (entity.getUsuario() != null)
                ? entity.getUsuario().getNomeCompleto()
                : "";

        List<String> especialidades = parseStringArray(entity.getEspecialidades());
        List<Integer> diasAtendimento = parseIntArray(entity.getDiasAtendimento());

        Email emailProfissional = Email.of(entity.getEmailProfissional());

        Endereco endereco = Endereco.of(
                entity.getEnderecoRua(),
                entity.getEnderecoNumero(),
                entity.getEnderecoBairro(),
                entity.getEnderecoCidade(),
                entity.getEnderecoEstado(),
                entity.getEnderecoCep()
        );

        return Medico.reconectar(
                entity.getId(),
                entity.getUsuarioId(),
                nomeCompleto,
                entity.getCrmNumero(),
                entity.getCrmEstado(),
                entity.getCrmSituacao(),
                entity.getCrmDataEmissao(),
                entity.getDataNascimento(),
                entity.getSexo(),
                entity.getNacionalidade(),
                emailProfissional,
                endereco,
                especialidades,
                entity.getFotoUrl(),
                entity.getLocalizacao(),
                diasAtendimento,
                entity.getHorarioInicioManha(),
                entity.getHorarioFimManha(),
                entity.getHorarioInicioTarde(),
                entity.getHorarioFimTarde(),
                entity.getIntervaloConsultaMinutos(),
                entity.getSemanasDisponibilidade(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao(),
                entity.getAtivo()
        );
    }

    private static List<String> parseStringArray(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static List<Integer> parseIntArray(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<List<Integer>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
