package UtilsTests;

import models.entities.Employee;
import models.enums.Privilege;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JWTUtil;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilTest {

    private JWTUtil jwtUtil;
    private Employee employee;

    @BeforeEach
    public void setup() {
        jwtUtil = new JWTUtil();
        employee = new Employee();
        employee.setId(1L);
        employee.setUsername("testUser");
        employee.setEmail("testUser@example.com");
        employee.setPhone("1234567890");
        employee.setPrivilege(Privilege.HR);
    }

    @Test
    void testGenerateSignedJWT() {
        String jwt = jwtUtil.generateSignedJWT(employee);
        assertNotNull(jwt);
        assertFalse(jwt.isEmpty());
    }

    @Test
    void testVerifyJWT() {
        String jwt = jwtUtil.generateSignedJWT(employee);
        assertNotNull(jwtUtil.verifyJWT(jwt, Privilege.HR));
    }
}