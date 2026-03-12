#!/bin/bash

# Script to add newStubMethod to all remaining Java files

# Files to process (remaining from the full list)
files=(
    "./src/test/java/rest/RestResponse1.java"
    "./src/test/java/rest/RestTest1.java"
    "./src/test/java/rest/pojos/CreateUserResponse1.java"
    "./src/test/java/rest/pojos/UserLogin1.java"
    "./src/test/java/rest/pojos/UserPojo1.java"
    "./src/test/java/rest/pojos/UserPojoFull1.java"
    "./src/test/java/rest/pojos/UserRequest1.java"
    "./src/test/java/rest/steps/UsersSteps1.java"
    "./src/test/java/simple/NewTest1.java"
    "./src/test/java/simple/automation/AnotherGeneratedTest1.java"
    "./src/test/java/simple/automation/BasicTest1.java"
    "./src/test/java/simple/automation/CalculatorTest1.java"
    "./src/test/java/simple/automation/DataProcessorTest1.java"
    "./src/test/java/simple/automation/EmailValidatorTest1.java"
    "./src/test/java/simple/automation/ExampleTest1.java"
    "./src/test/java/simple/automation/GeneratedTest1.java"
    "./src/test/java/simple/automation/MainTest1.java"
    "./src/test/java/simple/automation/SampleTest1.java"
    "./src/test/java/simple/automation/SmokeTest1.java"
    "./src/test/java/simple/automation/StringUtilsTest1.java"
    "./src/test/java/simple/automation/TemplateEngineTest1.java"
    "./src/test/java/simple/automation/ThirdGeneratedTest1.java"
    "./src/test/java/utils/DateDeserializer1.java"
    "./src/test/java/utils/RestWrapper1.java"
    "./src/test/java/utils/UserGenerator1.java"
    "./src/test/java/utils/services/OrderService1.java"
    "./src/test/java/utils/services/RestService1.java"
    "./src/test/java/utils/services/UserService1.java"
    "./src/test/java/web/WebDriverLogger1.java"
    "./src/test/java/web/findby/JSExecutor1.java"
    "./src/test/java/web/findby/RamblerTest1.java"
    "./src/test/java/web/findby/WebDriverFactory1.java"
    "./src/test/java/web/findby/config/TestConfigFactory1.java"
    "./src/test/java/web/findby/config/WebConfig1.java"
    "./src/test/java/web/findby/elements/Button1.java"
    "./src/test/java/web/findby/elements/CustomElement1.java"
    "./src/test/java/web/findby/elements/EditBox1.java"
    "./src/test/java/web/findby/elements/ElementsDecorator1.java"
    "./src/test/java/web/findby/elements/IFrame1.java"
    "./src/test/java/web/findby/page/BaseLoggedInPage1.java"
    "./src/test/java/web/findby/page/BasePage1.java"
    "./src/test/java/web/findby/page/LoginPage1.java"
    "./src/test/java/web/findby/page/ProfilePage1.java"
    "./src/test/java/web/findby/page/SearchPage1.java"
    "./src/test/java/web/findby/tests/BaseTest1.java"
    "./src/test/java/web/findby/tests/LoginTest1.java"
    "./src/test/java/web/findby/tests/ProfileTest1.java"
    "./src/test/java/web/simple/RamblerTest1.java"
    "./src/test/java/web/simple/ScreenshotExtension1.java"
    "./src/test/java/web/simple/WebDriverFactory1.java"
    "./src/test/java/web/simple/config/TestConfigFactory1.java"
    "./src/test/java/web/simple/config/WebConfig1.java"
    "./src/test/java/web/simple/page/BaseLoggedInPage1.java"
    "./src/test/java/web/simple/page/BasePage1.java"
    "./src/test/java/web/simple/page/LoginPage1.java"
    "./src/test/java/web/simple/page/ProfilePage1.java"
    "./src/test/java/web/simple/page/SearchPage1.java"
)

count=0

for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        # Check if newStubMethod already exists
        if grep -q "newStubMethod" "$file"; then
            echo "Skipping $file - already has newStubMethod"
            continue
        fi

        # Create temp file with added method
        # Add the stub method before the last closing brace
        sed '$s/}/    public void newStubMethod() {\n        \/\/ TODO: implement\n    }\n}/' "$file" > "$file.tmp"

        if [ $? -eq 0 ]; then
            mv "$file.tmp" "$file"
            echo "Modified: $file"
            ((count++))
        else
            echo "Error processing: $file"
            rm -f "$file.tmp"
        fi
    else
        echo "File not found: $file"
    fi
done

echo ""
echo "Total files modified: $count"
