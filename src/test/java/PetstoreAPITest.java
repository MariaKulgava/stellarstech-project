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
    private static int randomId;

    @BeforeClass
    public void setup() {
        playwright = Playwright.create();
        apiRequestContext = playwright.request().newContext(
                new APIRequest.NewContextOptions().setBaseURL("https://petstore.swagger.io/v2/")
        );

        // Generate random username
        randomUsername = DataGenerator.generateRandomUsername();
        randomId = DataGenerator.generateRandomId();
    }

    @Test(priority = 1)
    public void testCreateUser() {
        String requestBody = """
                {
                    "id": %d,
                    "username": "%s",
                    "firstName": "Test",
                    "lastName": "User",
                    "email": "testuser@example.com",
                    "password": "password123",
                    "phone": "1234567890",
                    "userStatus": 1
                }
                """.formatted(randomId, randomUsername);

        APIResponse response = apiRequestContext.post(
                "user",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(requestBody)
        );

        assertEquals(response.status(), 200, "The response status should be 200");
        assertTrue(response.text().contains("\"message\":\"" + randomId + "\""),
                "Response should contain 'message: " + randomId + "'");
        System.out.println("User created with username: " + randomUsername + " and ID: " + randomId);
    }

    @Test(priority = 2, dependsOnMethods = "testCreateUser")
    public void testGetUser() {
        APIResponse response = apiRequestContext.get("user/" + randomUsername);

        assertEquals(response.status(), 200, "The response status should be 200");
        assertTrue(response.text().contains(randomUsername), "The response should contain the username");
        assertTrue(response.text().contains("\"id\":" + randomId),
                "Response should contain 'id: " + randomId + "'");
        System.out.println("User exists: " + randomUsername + " and ID: " + randomId);
    }

    @Test(priority = 3, dependsOnMethods = "testGetUser")
    public void testDeleteUser() {
        APIResponse response = apiRequestContext.delete("user/" + randomUsername);

        assertEquals(response.status(), 200, "The response status should be 200");
        System.out.println("User deleted: " + randomUsername);
    }

    @Test(priority = 4, dependsOnMethods = "testDeleteUser")
    public void testGetUserNotFound() {
        APIResponse response = apiRequestContext.get("user/" + randomUsername);

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
