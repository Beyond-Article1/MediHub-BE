package mediHub_be.journal.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

// List를 저장하는 컨버터
@Converter
    public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> dataList) {
        try {
            return mapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String data) {
        try {
            if (data == null || data.trim().isEmpty()) {
                return List.of(); // 빈 리스트를 반환
            }
            // JSON 배열이 아닌 경우, 단일 문자열을 리스트로 감싸서 반환
            if (!data.startsWith("[") && !data.endsWith("]")) {
                return List.of(data); // 단일 문자열을 리스트로 반환
            }
            // JSON 배열 형식인 경우 정상적으로 파싱
            return mapper.readValue(data, List.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
