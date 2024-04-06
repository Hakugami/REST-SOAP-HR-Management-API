package services.impl;

import lombok.extern.slf4j.Slf4j;
import utils.ApiUtil;

import java.util.Set;

@Slf4j
public class DummyMain {
    public static void main(String[] args) {
        Set<String> fields = ApiUtil.getFields("phone,email,jobTitle,salary,isHired");
        log.info(String.valueOf(EmployeeService.getInstance().employeePartialResponse(102L, fields)));
    }
}
