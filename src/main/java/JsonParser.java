import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

    public static Object parseJson(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);
        return parseNode(rootNode);
    }

    private static Object parseNode(JsonNode node) {
        if (node.isObject()) {
            Map<String, Object> map = new HashMap<>();
            node.fieldNames().forEachRemaining(field -> map.put(field, parseNode(node.get(field))));
            return map;
        } else if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            node.forEach(element -> list.add(parseNode(element)));
            return list;
        } else if (node.isBigInteger()) {
            return node.bigIntegerValue();
        } else if (node.isBigDecimal() || node.isFloatingPointNumber()) {
            return node.decimalValue();
        } else if (node.isInt() || node.isLong()) {
            return BigInteger.valueOf(node.longValue());
        } else if (node.isTextual()) {
            return node.textValue();
        } else if (node.isBoolean()) {
            return node.booleanValue();
        } else if (node.isNull()) {
            return null;
        }
        throw new IllegalArgumentException("Unsupported JSON node type: " + node.getNodeType());
    }

    public static void main(String[] args) {
        try {
            String jsonString = """
                    {
                        "name": "John Doe",
                        "age": 25,
                        "salary": 12345.67,
                        "isEmployee": true,
                        "skills": ["Java", "Kotlin", "Scala"],
                        "details": {
                            "joinedOn": "2020-01-01",
                            "experience": 5.5
                        }
                    }
                    """;
            Object result = parseJson(jsonString);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
