package EndPointsTests;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.DTO.LoginDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttendanceControllerTest {
    private Client client;
    private static final String BASE_URL = "http://localhost:4545/API/webapi/attendance";
    private Jsonb jsonb;
    private String token;

@BeforeEach
public void setup() {
    client = ClientBuilder.newClient();
    jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));

    // Create a LoginDto with valid credentials
    LoginDto loginDto = new LoginDto();
    loginDto.setUsername("lamebame");
    loginDto.setPassword("pass123");

    // Make a POST request to the /auth/login endpoint
    Response loginResponse = client.target("http://localhost:4545/API/webapi/auth/login")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(loginDto, MediaType.APPLICATION_JSON));

    // Extract the token from the Authorization header of the response
    String authHeader = loginResponse.getHeaderString("Authorization");
    token = authHeader.substring("Bearer ".length());
}

    @AfterEach
    public void teardown() {
        client.close();
    }

    @Test
    void shouldMarkAttendance() {
        // Assuming you have a valid JWT token for the test


        Response response = client.target(BASE_URL)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .post(Entity.text(""));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldGetAttendance() {


        Response response = client.target(BASE_URL)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}