import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.DataGenerator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class PetstoreAPITest {
    public static Playwright playwright;
    public static APIRequestContext apiRequestContext;
    private static String randomUsername;

    @BeforeClass
    public void setup() {
        playwright = Playwright.create();
        apiRequestContext = playwright.request().newContext(
                new APIRequest.NewContextOptions().setBaseURL("https://petstore.swagger.io")
        );

        // Generate random username
        randomUsername = DataGenerator.generateRandomUsername();
    }

    @Test(priority = 1)
    public void testCreateUser() {
        String requestBody = """
                {
                    "id": 0,
                    "username": "%s",
                    "firstName": "Test",
                    "lastName": "User",
                    "email": "testuser@example.com",
                    "password": "password123",
                    "phone": "1234567890",
                    "userStatus": 1
                }
                """.formatted(randomUsername);

        APIResponse response = apiRequestContext.post(
                "/v2/user",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(requestBody)
        );

        assertEquals(response.status(), 200, "The response status should be 200");
        System.out.println("User created with username: " + randomUsername);
    }

    @Test(priority = 2, dependsOnMethods = "testCreateUser")
    public void testGetUser() {
        APIResponse response = apiRequestContext.get("/v2/user/" + randomUsername);

        assertEquals(response.status(), 200, "The response status should be 200");
        assertTrue(response.text().contains(randomUsername), "The response should contain the username");
        System.out.println("User exists: " + randomUsername);
    }

    @Test(priority = 3, dependsOnMethods = "testGetUser")
    public void testDeleteUser() {
        APIResponse response = apiRequestContext.delete("/v2/user/" + randomUsername);

        assertEquals(response.status(), 200, "The response status should be 200");
        System.out.println("User deleted: " + randomUsername);
    }

    @Test(priority = 4, dependsOnMethods = "testDeleteUser")
    public void testGetUserNotFound() {
        APIResponse response = apiRequestContext.get("/user/" + randomUsername);

        assertEquals(response.status(), 404, "The response status should be 404");
        System.out.println("User not found: " + randomUsername);
    }

    @AfterClass
    public void tearDown() {
        if (apiRequestContext != null) {
            apiRequestContext.dispose();
        }
        playwright.close();
    }
}
