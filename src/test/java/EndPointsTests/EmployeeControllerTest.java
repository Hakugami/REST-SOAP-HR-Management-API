package EndPointsTests;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.DTO.EmployeeDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeControllerTest {
    private static final String Reasons = "no need for this test to be ran everytime";
    private Client client;
    private static final String BASE_URL = "http://localhost:4545/API/webapi/employees";
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
    void shouldGetEmployee() {
        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .get();
        String prettyJson = jsonb.toJson(response.readEntity(Object.class));
        System.out.println("Response: " + prettyJson);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldGetAllEmployeesJson() {
        Response response = client.target(BASE_URL+"?type=json&limit=1")
                .request(MediaType.APPLICATION_JSON)
                .get();
        String prettyJson = jsonb.toJson(response.readEntity(Object.class));
        System.out.println("Response: " + prettyJson);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldGetAllEmployeesXml() {
        Response response = client.target(BASE_URL+"?type=xml&limit=1")
                .request(MediaType.APPLICATION_XML)
                .get();
        String prettyXml = formatXml(response.readEntity(String.class));
        System.out.println("Response: " + prettyXml);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    private String formatXml(String xml) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(new StringWriter());
            StreamSource source = new StreamSource(new StringReader(xml));
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Disabled(Reasons)
    @Test
    void shouldCreateEmployee() {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("testFirstName")
                .lastName("testLastName")
                .email("test@test.com")
                .phone("1234567890")
                .username("testUser")
                .password("testPassword")
                .birthDate(LocalDate.parse("1990-01-01"))
                .yearsOfExperience(5)
                .build();

        Response response = client.target(BASE_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(employeeDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Disabled(Reasons)
    @Test
    void shouldUpdateEmployee() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("testFirstName");
        employeeDto.setLastName("testLastName");
        // Set properties of employeeDto

        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(employeeDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Disabled(Reasons)
    @Test
    void shouldDeleteEmployee() {
        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
