package br.com.bootcamp.tayBank.converters;

import br.com.bootcamp.tayBank.enums.StatusPropostaEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StatusPropostaConverter implements AttributeConverter<StatusPropostaEnum, String> {

    @Override
    public String convertToDatabaseColumn(StatusPropostaEnum attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public StatusPropostaEnum convertToEntityAttribute(String dbData) {
        return StatusPropostaEnum.parse(dbData);
    }
}
