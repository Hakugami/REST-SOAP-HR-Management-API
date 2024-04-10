package EndPointsTests;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.DTO.DepartmentDto;
import models.entities.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepartmentControllerTest {
    private Client client;
    private static final String BASE_URL = "http://localhost:4545/API/webapi/departments";
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
    void shouldGetDepartment() {
        Response response = client.target(BASE_URL + "/2")
                .request(MediaType.APPLICATION_JSON)
                .get();
        String prettyJson = jsonb.toJson(response.readEntity(Object.class));
        System.out.println("Response: " + prettyJson);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldGetAllDepartmentsJson() {
        Response response = client.target(BASE_URL+"?type=json&limit=1")
                .request(MediaType.APPLICATION_JSON)
                .get();
        String prettyJson = jsonb.toJson(response.readEntity(Object.class));
        System.out.println("Response: " + prettyJson);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldGetAllDepartmentsXml() {
        Response response = client.target(BASE_URL+"?type=xml&limit=1")
                .request(MediaType.APPLICATION_XML)
                .get();
        String prettyXml = formatXml(response.readEntity(String.class));
        System.out.println("Response: " + prettyXml);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldCreateDepartment() {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setName("Test Department");
        departmentDto.setActive(true);
        departmentDto.setDescription("Test Description");
        // Set properties of departmentDto

        Response response = client.target(BASE_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(departmentDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldUpdateDepartment() {
        DepartmentDto departmentDto = new DepartmentDto();
        // Set properties of departmentDto

        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(departmentDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldDeleteDepartment() {
        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldPatchDepartment() {
        DepartmentDto departmentDto = new DepartmentDto();
        // Set properties of departmentDto

        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(departmentDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldAssignManagerToDepartment() {
        Long managerId = 252L; // replace with actual manager id
        Employee manager = new Employee();

        Response response = client.target(BASE_URL + "/2/manager/" + managerId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(manager, MediaType.APPLICATION_JSON));

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
}