package EndPointsTests;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.DTO.JobDto;
import models.enums.JobTitle;
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
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JobControllerTest {
    private static final String Reasons = "no need for this test to be ran everytime";
    private static final String BASE_URL = "http://localhost:4545/API/webapi/jobs";
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
    void shouldGetJob() {
        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .get();
        String prettyJson = jsonb.toJson(response.readEntity(Object.class));
        System.out.println("Response: " + prettyJson);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldGetAllJobsJson() {
        Response response = client.target(BASE_URL + "?type=json&limit=1")
                .request(MediaType.APPLICATION_JSON)
                .get();
        String prettyJson = jsonb.toJson(response.readEntity(Object.class));
        System.out.println("Response: " + prettyJson);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void shouldGetAllJobsXml() {
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
    void shouldCreateJob() {
        JobDto jobDto = JobDto.builder()
                .title(JobTitle.DEVELOPER)
                .description("testDescription")
                .available(true)
                .maxSalary(BigDecimal.valueOf(2000))
                .minExperience(5)
                .startingSalary(BigDecimal.valueOf(300))
                .build();

        Response response = client.target(BASE_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(jobDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    //    @Disabled(Reasons)
    @Test
    void shouldUpdateJob() {
        JobDto jobDto = new JobDto();
        jobDto.setTitle(JobTitle.DEVELOPER);
        jobDto.setDescription("testDescription");
        jobDto.setAvailable(true);
        jobDto.setMaxSalary(BigDecimal.valueOf(1000));
        jobDto.setMinExperience(5);
        jobDto.setStartingSalary(BigDecimal.valueOf(500));

        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(jobDto, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    //    @Disabled(Reasons)
    @Test
    void shouldDeleteJob() {
        Response response = client.target(BASE_URL + "/1")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }


}