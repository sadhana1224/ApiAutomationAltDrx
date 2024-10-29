package tests;

import com.github.javafaker.Faker;
import io.restassured.response.Response; // Use RestAssured's Response
import endpoints.UserAEndpoints;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payload.User;
import io.restassured.RestAssured;
import utilities.Reports;

import static utilities.Reports.setTCDesc;

public class UserTests extends Reports {
    Faker faker;
    User userPayload;
    @DataProvider(name = "userData")
    public Object[][] userData() {
        return new Object[][] {
                { 254,900,"Sadhana","test bodyy test test"},
                { 255,901,"Subashini","test bodyy test Automation test"},
                {289,903,"Logesh","Header body footer" }
                // Add more test data as needed
        };
    }
    /*
@BeforeClass //i will pass the value to pojo class
    public void setupData()
    {

        faker=new Faker();
        userPayload =new User();

        userPayload.setUserId(faker.idNumber().hashCode());//randaomly generate the id number
        userPayload.setId(faker.idNumber().hashCode());
        userPayload.setTitle(faker.name().name());
        userPayload.setBody(faker.lorem().sentence());
    } */
    @Test
    public void testGetPosts() {
        setTCDesc("Verify the URL");
        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts");

        // Assert that the status code is 200
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    //post requestes TCs
    @Test(dataProvider = "userData",priority = 1)
   public void testPostUser(int userId,int id, String title, String body)
   {
       setTCDesc("Test to Post a New User");
       User userPayload = new User();
       userPayload.setUserId(userId);
       userPayload.setId(id);
       userPayload.setTitle(title);
       userPayload.setBody(body);
     Response response =UserAEndpoints.createUser(userPayload);
     response.then().log().all();
     Assert.assertEquals(response.getStatusCode(),201);
       int createdId = response.jsonPath().getInt("id");
       userPayload.setId(createdId); // Set the correct ID for subsequent tests

   }

    @Test(priority = 2)
    public void testGetUserById()
    {

        setTCDesc("Test to Get User by ID");
        int idToRead = 7;
        System.out.println("Fetching user with ID: " + idToRead);

        Response response = UserAEndpoints.ReadUser(idToRead);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);

        // Extract and validate the response data
        String title = response.jsonPath().getString("title");
        String body = response.jsonPath().getString("body");

        // Validate against expected values for ID 7
        Assert.assertEquals(title, "magnam facilis autem");
        Assert.assertEquals(body, "dolore placeat quibusdam ea quo vitae\nmagni quis enim qui quis quo nemo aut saepe\nquidem repellat excepturi ut quia\nsunt ut sequi eos ea sed quas");
    }
    @Test(priority = 3)

        public void testUpdateUserById()
    {

        userPayload =new User();
        setTCDesc("Test to Update User by ID");
        int existingUserId = 10;  // Use an ID you know exists in your test setup
       // userPayload.setId(existingUserId);
        userPayload.setUserId(2265);
        userPayload.setId(existingUserId);
        userPayload.setTitle("updated.email@example.com");
        userPayload.setBody("This is an updated body for testing.");

        // Update user data using payload
        Response response = UserAEndpoints.UpdateUser(existingUserId, userPayload);
        response.then().log().all();

        // Validate update response status code
        Assert.assertEquals(response.getStatusCode(), 200);

        // Checking data after update
        int idToRead = userPayload.getId();
        Response responseAfterUpdate = UserAEndpoints.ReadUser(idToRead);
        responseAfterUpdate.then().log().all();
        Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);


    }
    @Test(priority =4)
public void testDeleteUserById()
    {
        setTCDesc("Test to Delete User by ID");
        int userIdToDelete = 9;
        Response response =UserAEndpoints.DeleteUser(userIdToDelete);
        Assert.assertEquals(response.getStatusCode(),200);
    }



    }

