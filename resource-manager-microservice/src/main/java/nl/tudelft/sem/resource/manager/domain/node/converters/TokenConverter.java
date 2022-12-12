package nl.tudelft.sem.resource.manager.domain.node.converters;

import nl.tudelft.sem.resource.manager.domain.node.Token;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class TokenConverter implements AttributeConverter<Token, String> {
    @Override
    public String convertToDatabaseColumn(Token attribute) {
        return attribute.toString();
    }

    @Override
    public Token convertToEntityAttribute(String dbData) {
        return new Token(dbData);
    }
}
