package generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestCodeGenerator {

    public String extractPlayWrightCode(String llmResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(llmResponse);
            JsonNode choicesNode = rootNode.path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode messageNode = choicesNode.get(0).path("message");
                String content = messageNode.path("content").asText().trim();
                String startingText = "```playwright";
                if (content.contains(startingText)) {
                    int start = content.indexOf(startingText);
                    int end = content.lastIndexOf("```");
                    if (start != -1 && end != -1 && end > start) {
                        content = content.substring(start + startingText.length(), end).trim();
                    }
                }else {
                    throw new NullPointerException("Check the output : " + content);
                }
                return content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertToJavaCode(llmResponse);
    }

    public String convertToJavaCode(String extractedCode) {
        return addMissingImports(extractedCode);
    }

    /**
     * Ensures all required Rest Assured and Hamcrest imports are included in the generated code.
     */
    private String addMissingImports(String javaCode) {
        String requiredImports = """
            import io.restassured.RestAssured;
            import io.restassured.http.ContentType;
            import io.restassured.response.Response;
            import org.testng.annotations.BeforeMethod;
            import org.testng.annotations.Test;
            import static io.restassured.RestAssured.given;
            import static org.testng.Assert.assertEquals;
            import static org.hamcrest.Matchers.equalTo;
            import static org.hamcrest.Matchers.notNullValue;
            """;

        if (!javaCode.contains("import io.restassured.RestAssured;")) {
            javaCode = requiredImports + "\n" + javaCode;
        }
        return javaCode;
    }
}
