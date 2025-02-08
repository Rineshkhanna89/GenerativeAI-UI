package generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import utilities.FileUtils;
import java.util.*;

public class LLMTestGenerator {
    private String llmAPIURL;
    private String apiKey;
    private String model;
    private String temperature;

            public LLMTestGenerator(){
                Properties llmProperties = FileUtils.readProperty(".//src//main//resources//LLM.properties");
                llmAPIURL = llmProperties.getProperty("LLM_API_URL");
                    apiKey = llmProperties.getProperty("API_KEY");
                    model = llmProperties.getProperty("model");
                    temperature = llmProperties.getProperty("temperature");
            }

    public String generatePlayWrightCode(String seleniumFileAsString) {
        if (seleniumFileAsString == null || seleniumFileAsString.isEmpty()) {
            return "No valid Selenium filename to convert into playwright test";
        }
        String userPrompt = "Generate the executable playwright javascript code for the given selenium code:\n"
                            + seleniumFileAsString;
        try {
            List<Map<String, String>> messages = getMaps(userPrompt);
            Map<String, Object> payload = new HashMap<>();
            payload.put("model", model);
            payload.put("messages", messages);
            payload.put("temperature", Float.parseFloat(temperature));
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(payload);
            return callLLMApi(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error building JSON payload: " + e.getMessage();
        }
    }

    private List<Map<String, String>> getMaps(String userPrompt) {
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content","You are a helpful assistant that converts Java Selenium code to exact Playwright code"
                + "- Contain only Playwright code enclosed in a single code block using triple backticks (```playwright ... ```)"
                + "- Provide an executable Playwright test by defining all necessary functions correctly."
                + "- Use correct imports like test, expect and fixture injection to avoid page.goto error"
                + "- Never declare `page` manually. Instead, always use `{ page }` from Playwright’s test function."
                + "- Never include manual `chromium.launch()` or `browser.close()` as it causes issues."
                + "- Use `getByText()` ONLY for XPath-based text locators instead of locator"
                + "- Strictly avoid `querySelector()` as it results in flaky tests."
                + "- Wrap the test inside Playwright’s `test()` function properly."
                + "- Ensure Playwright locators are correctly mapped from Selenium locators and do not add any new text."
                + "- Use `await expect(locator).toBeVisible()` instead of `await page.isVisible()`."
                + "- Use `await page.waitForURL()` instead of `waitForNavigation()`."
                + "- Do not repeat function definitions, do not add additional empty test blocks."
                + "- The test should be named correctly and stored in a file ending with `.spec.js` to avoid 'no tests found' errors."
                + "- Avoid `describe()`, `beforeEach()`, or `class` unless explicitly needed, as they sometimes return 'not defined' errors."
                + "- Do not include any additional test cases other than the positive flow."
                + "Strictly follow all these instructions to ensure accurate Playwright conversion.");
        messages.add(systemMessage);
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userPrompt);
        messages.add(userMessage);
        return messages;
    }

    private String callLLMApi(String requestBody) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(llmAPIURL);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "Bearer " + apiKey);
            request.setEntity(new StringEntity(requestBody));
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error calling LLM API: " + e.getMessage();
        }
    }
}
