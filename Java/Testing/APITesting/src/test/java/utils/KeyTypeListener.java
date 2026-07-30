package utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import base.BaseTest;

import java.lang.reflect.Method;

public class KeyTypeListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if(method.isTestMethod()) {
            Method testMethod = method.getTestMethod().getConstructorOrMethod().getMethod();

            KeyType keyType = KeyType.PUBLIC;
            if (testMethod.isAnnotationPresent(UseKey.class)) {
                keyType = testMethod.getAnnotation(UseKey.class).value();
            }

            Object testInstance = testResult.getInstance();
            if(testInstance instanceof BaseTest) {
                ((BaseTest) testInstance).requestSpec = new RequestSpecBuilder()
                        .setBaseUri("https://reqres.in")
                        .setBasePath("/api")
                        .addHeader("x-api-key", ConfigManager.getAPiKey(keyType))
                        .setContentType(ContentType.JSON)
                        .setAccept(ContentType.JSON)
                        .addFilter(new RequestLoggingFilter())
                        .addFilter(new ResponseLoggingFilter())
                        .build();
            }

        }
    }
}
