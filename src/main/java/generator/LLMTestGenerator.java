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
        
        // userPrompt to Generate the PLayWright JavaScript Code
        String userPrompt = "Generate the executable playwright javascript code for the given selenium code:\n"
                            + seleniumFileAsString;
        
        //userPrompt to Generate the PLayWright TypeScript Code
//        String userPrompt = "Generate the executable playwright typescript code for the given selenium java code:\n"
//                + seleniumFileAsString;
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
        
        //Prompt for JavaScript
		
		  systemMessage.put( "content","You are a test automation expert that converts Java Selenium code to exact Playwright code"
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
		  + "Strictly follow all these instructions to ensure accurate Playwright conversion."
		  );
		 
        //Prompt for TypeScript
        
		/*
		 * systemMessage.put("content","Instructions:\n" + "\n" +
		 * "- Convert Selenium Java test automation code to Playwright TypeScript while preserving the logic and functionality.\n"
		 * +
		 * "- Ensure that the converted code follows Playwright's best practices, including:\n"
		 * + "    -- Proper async/await usage for handling asynchronous operations.\n" +
		 * "    -- Selectors conversion (e.g., By.id() → page.locator() equivalent).\n"
		 * +
		 * "    -- Handling of waits (Implicit/Explicit waits should be replaced with Playwright’s auto-waiting).\n"
		 * +
		 * "    -- Assertions should be mapped to Playwright’s test assertions if applicable.\n"
		 * +
		 * "    -- Maintain proper TypeScript typings (Page, Browser, etc.) and use ES6+ features where appropriate.\n"
		 * +
		 * "    -- Optimize code structure, removing unnecessary waits or redundant calls.\n"
		 * +
		 * "    -- Ensure that logging/debugging mechanisms (if present in Selenium) are mapped correctly to Playwright equivalents.\n"
		 * +
		 * "    -- The output must be idiomatic Playwright TypeScript, not just a direct Java-to-TypeScript translation.\n"
		 * +
		 * "    -- Follow Playwright Official Documentation to ensure all functions are correctly\n"
		 * + "    -- DO NOT add any additional steps other than given input code.\n" +
		 * "    -- Disable strict mode violation when finding locators\n" +
		 * "    -- Make sure to waitUntil: 'domcontentloaded' \n" + "\n" + "Context:\n"
		 * + "\n" +
		 * "I am building an AI-based prompt to convert Selenium Java code to Playwright TypeScript automatically.\n"
		 * +
		 * "The converted code must be production-ready, as accuracy is crucial for my career growth.\n"
		 * + "\n" + "Example:\n" + "\n" + "Selenium Java (Input)\n" + "\n" + "java\n" +
		 * "import org.openqa.selenium.WebDriver;\n" +
		 * "import org.openqa.selenium.chrome.ChromeDriver;\n" + "\n" +
		 * "public class PrintTitle {\n" +
		 * "  public static void main(String[] args) {\n" +
		 * "    WebDriver driver = new ChromeDriver();\n" +
		 * "    driver.get(\"http://playwright.dev\");\n" +
		 * "    System.out.println(driver.getTitle());\n" + "    driver.quit();\n" +
		 * "  }\n" + "}\n" + "\n" + "\n" + "Playwright TypeScript (Expected Output)\n" +
		 * "\n" + "typescript\n" + "import { test, expect } from '@playwright/test';\n"
		 * + "\n" + "test('has title', async ({ page }) => {\n" +
		 * "  await page.goto('https://playwright.dev/');\n" + "\n" +
		 * "  // Expect a title \"to contain\" a substring.\n" +
		 * "  await expect(page).toHaveTitle(/Playwright/);\n" + "});\n" + "\n" + "\n" +
		 * "Persona:\n" + "\n" +
		 * "You are a Senior Test Automation Architect specializing in Selenium and Playwright migration. \n"
		 * +
		 * "Your responsibility is to ensure that the converted Playwright TypeScript code is accurate, maintainable, and follows industry best practices.\n"
		 * + "\n" + "Output Format:\n" + "\n" +
		 * "-   The output should be fully working Playwright TypeScript code.\n" +
		 * "-   It should be structured as an executable script or within a test framework if required.\n"
		 * +
		 * "-   The code should be formatted properly and follow Playwright’s official documentation. \n"
		 * +
		 * "-   DO NOT Provide anything other than Playwright Code Such as explanations, Key Points.\n"
		 * + "-   Make Sure the comments are staying as it is in the code.\n" + "\n" +
		 * "Use the above framework to generate the playwright typescript code from the input java code"
		 * );
		 */        
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
