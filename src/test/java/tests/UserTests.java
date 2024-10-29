package tests;

import com.github.javafaker.Faker;
import io.restassured.response.Response; // Use RestAssured's Response
import endpoints.UserAEndpoints;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import payload.User;
import io.restassured.RestAssured;
import utilities.Reports;

import static utilities.Reports.setTCDesc;

public class UserTests extends Reports {
    Faker faker;
    User userPayload;

@BeforeClass //i will pass the value to pojo class
    public void setupData()
    {

        faker=new Faker();
        userPayload =new User();

        userPayload.setUserId(faker.idNumber().hashCode());//randaomly generate the id number
        userPayload.setId(faker.idNumber().hashCode());
        userPayload.setTitle(faker.name().name());
        userPayload.setBody(faker.lorem().sentence());
    }
    @Test
    public void testGetPosts() {
        setTCDesc("Verify the URL");
        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts");

        // Assert that the status code is 200
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    //post requestes TCs
    @Test(priority = 1)
   public void testPostUser()
   {
       setTCDesc("Test to Post a New User");
     Response response =UserAEndpoints.createUser(userPayload);
     response.then().log().all();
     Assert.assertEquals(response.getStatusCode(),201);
       int createdId = response.jsonPath().getInt("id");
       userPayload.setId(createdId); // Set the correct ID for subsequent tests

   }
    @Test(priority = 2)
    public void testGetUserById()
    {
     /*   int idToRead = userPayload.getId();
        System.out.println("Fetching user with ID: " + idToRead);

        Response response =  UserAEndpoints.ReadUser(idToRead);
        response.then().log().all();
        if (response.getStatusCode() == 404) {
            System.out.println("User with ID " + idToRead + " does not exist.");
        }else {
            Assert.assertEquals(response.getStatusCode(), 200);
        }

      */
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

        setTCDesc("Test to Update User by ID");
        int existingUserId = 10;  // Use an ID you know exists in your test setup
        userPayload.setId(existingUserId);

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

