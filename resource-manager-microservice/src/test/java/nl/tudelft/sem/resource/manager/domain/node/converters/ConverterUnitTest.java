package nl.tudelft.sem.resource.manager.domain.node.converters;

import nl.tudelft.sem.resource.manager.domain.node.OwnerName;
import nl.tudelft.sem.resource.manager.domain.node.Token;
import nl.tudelft.sem.resource.manager.domain.node.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConverterUnitTest {

    private transient OwnerNameConverter ownerNameConverter;
    private transient TokenConverter tokenConverter;
    private transient URLConverter urlConverter;

    private transient OwnerName ownerName;
    private transient Token token;
    private transient URL url;

    @BeforeEach
    public void setup() {
        ownerNameConverter = new OwnerNameConverter();
        tokenConverter = new TokenConverter();
        urlConverter = new URLConverter();

        ownerName = new OwnerName("John Doe");
        token = new Token("tokenValue");
        url = new URL("urlValue");
    }

    @Test
    void convertToDatabaseColumn() {
        assertThat(ownerNameConverter.convertToDatabaseColumn(ownerName)).isEqualTo("John Doe");
        assertThat(tokenConverter.convertToDatabaseColumn(token)).isEqualTo("tokenValue");
        assertThat(urlConverter.convertToDatabaseColumn(url)).isEqualTo("urlValue");
    }

    @Test
    void convertToEntityAttribute() {
        assertThat(ownerNameConverter.convertToEntityAttribute("John Doe")).isEqualTo(ownerName);
        assertThat(tokenConverter.convertToEntityAttribute("tokenValue")).isEqualTo(token);
        assertThat(urlConverter.convertToEntityAttribute("urlValue")).isEqualTo(url);
    }
}