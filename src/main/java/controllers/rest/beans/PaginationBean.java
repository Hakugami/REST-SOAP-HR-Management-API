package controllers.rest.beans;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationBean {
    @QueryParam("offset")
    @DefaultValue("0")
    private int offset;
    @QueryParam("limit")
    @DefaultValue("10")
    private int limit;

}
