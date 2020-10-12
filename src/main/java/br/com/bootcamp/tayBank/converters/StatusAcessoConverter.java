package br.com.bootcamp.tayBank.converters;

import br.com.bootcamp.tayBank.enums.StatusAcessoEnum;
import br.com.bootcamp.tayBank.enums.StatusPropostaEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StatusAcessoConverter implements AttributeConverter<StatusAcessoEnum, String> {

    @Override
    public String convertToDatabaseColumn(StatusAcessoEnum attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public StatusAcessoEnum convertToEntityAttribute(String dbData) {
        return StatusAcessoEnum.parse(dbData);
    }
}
