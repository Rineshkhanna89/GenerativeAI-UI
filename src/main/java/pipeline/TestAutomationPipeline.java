package pipeline;

import generator.LLMTestGenerator;
import generator.TestCodeGenerator;
import utilities.FileUtils;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestAutomationPipeline {

    public static void main(String[] args) {
        //1. Convert the java-selenium file to a string
        Properties fileProperties = FileUtils.readProperty(".//src//main//resources//LLM.properties");
        String seleniumFile = fileProperties.getProperty("ipFilePathAndName"); // 1. Parse the Selenium File
        String fileAsString = FileUtils.convertFileToString(seleniumFile);

        // 2. Generate the equivalent PlayWright Code of Java-Selenium code using LLM
        LLMTestGenerator llmGen = new LLMTestGenerator();
        String llmRawOutput = llmGen.generatePlayWrightCode(fileAsString);

        // 3. Extract only the PlayWright code from the LLM response
        TestCodeGenerator codeGen = new TestCodeGenerator();
        String finalTestCode = codeGen.extractPlayWrightCode(llmRawOutput);

        // 4. Provide a class name from the generated PlayWright code
        String outputFileName = fileProperties.getProperty("opFileName");

        // 5. Write the generated code to a js file
        FileUtils.deleteFileIfExists(fileProperties.getProperty("opFilePath")+outputFileName);
        FileUtils.writeToFile(finalTestCode, fileProperties.getProperty("opFilePath")+outputFileName);
        System.out.println("Generated test code written to " + outputFileName);
    }

    /**
     * Extracts the Java class name from the given code.
     * It searches for a pattern matching "public class <ClassName>".
     */
    private static String extractClassName(String javaCode) {
        Pattern pattern = Pattern.compile("public\\s+class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(javaCode);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
