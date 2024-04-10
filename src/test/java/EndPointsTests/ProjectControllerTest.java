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
import models.DTO.ProjectDto;
import models.enums.ProjectStatus;
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

class ProjectControllerTest {
    private static final String Reasons = "no need for this test to be ran everytime";
    private static final String BASE_URL = "http://localhost:4545/API/webapi/projects";
    private Client client;
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
    void shouldGetProject() {
        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .get();
        String prettyJson = jsonb.toJson(response.readEntity(Object.class));
        System.out.println("Response: " + prettyJson);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldGetAllProjectsJson() {
        Response response = client.target(BASE_URL + "?type=json&limit=1")
                .request(MediaType.APPLICATION_JSON)
                .get();
        String prettyJson = jsonb.toJson(response.readEntity(Object.class));
        System.out.println("Response: " + prettyJson);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldGetAllProjectsXml() {
        Response response = client.target(BASE_URL + "?type=xml&limit=1")
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

    //    @Disabled(Reasons)
    @Test
    void shouldCreateProject() {
        ProjectDto projectDto = new ProjectDto();
        // Set properties of projectDto

        Response response = client.target(BASE_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(projectDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    //    @Disabled(Reasons)
    @Test
    void shouldUpdateProject() {
        ProjectDto projectDto = new ProjectDto();

        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(projectDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    //    @Disabled(Reasons)
    @Test
    void shouldDeleteProject() {
        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    //    @Disabled(Reasons)
    @Test
    void shouldPatchProject() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("testName");
        projectDto.setDescription("testDescription");
        projectDto.setTeamSize(5);
        projectDto.setDurationInMonths(6);
        projectDto.setStatus(String.valueOf(ProjectStatus.IN_PROGRESS));

        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(projectDto, MediaType.APPLICATION_JSON));


        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    //    @Disabled(Reasons)
    @Test
    void shouldAssignEmployeeToProject() {
        Long employeeId = 2L; // replace with actual employee id


        EmployeeDto employeeDto = new EmployeeDto();

        Response response = client.target(BASE_URL + "/1/employees/" + employeeId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(employeeDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}