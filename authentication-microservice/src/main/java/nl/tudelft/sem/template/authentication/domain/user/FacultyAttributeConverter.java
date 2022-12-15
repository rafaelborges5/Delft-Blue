package nl.tudelft.sem.template.authentication.domain.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Converter for the Faculty Enum.
 */
@Converter
public class FacultyAttributeConverter implements AttributeConverter<List<Faculty>, String> {

    @Override
    public String convertToDatabaseColumn(List<Faculty> attribute) {
        String faculties = attribute.toString();
        faculties = faculties.replace("[", "");
        faculties = faculties.replace("]", "");
        return faculties;
    }

    @Override
    public List<Faculty> convertToEntityAttribute(String dbData) {
        String[] facultyList = dbData.split(", ");
        List<Faculty> faculty = new ArrayList<>();
        for (String f : facultyList) {
            faculty.add(Faculty.valueOf(f));
        }
        return faculty;
    }

}

