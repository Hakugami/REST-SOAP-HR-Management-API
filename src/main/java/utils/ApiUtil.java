package utils;

import java.util.Set;

public class ApiUtil {

    private ApiUtil() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }
    public static Set<String> getFields(String fields) {
        return fields == null ? Set.of() : Set.of(fields.split(","));
    }
}
