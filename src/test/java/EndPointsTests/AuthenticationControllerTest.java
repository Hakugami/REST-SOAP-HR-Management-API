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

class AuthenticationControllerTest {
    private Client client;
    private static final String BASE_URL = "http://localhost:4545/API/webapi/auth";
    private Jsonb jsonb;

    @BeforeEach
    public void setup() {
        client = ClientBuilder.newClient();
        jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
    }

    @AfterEach
    public void teardown() {
        client.close();
    }

    @Test
    void shouldLogin() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("lamebame");
        loginDto.setPassword("pass123");

        Response response = client.target(BASE_URL + "/login")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(loginDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldNotLoginWithInvalidCredentials() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("invalidUser");
        loginDto.setPassword("invalidPassword");

        Response response = client.target(BASE_URL + "/login")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(loginDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }
}