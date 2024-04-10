//package controllers.rest.helpers.adapters;
//
//import jakarta.json.bind.serializer.DeserializationContext;
//import jakarta.json.bind.serializer.JsonbDeserializer;
//import jakarta.json.stream.JsonParser;
//
//import java.lang.reflect.Type;
//import java.time.LocalDate;
//
//public class LocalDateDeserializer implements JsonbDeserializer<LocalDate> {
//
//    @Override
//    public LocalDate deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
//        String dateStr = parser.getString();
//        if (dateStr == null || dateStr.isEmpty()) {
//            return null;
//        }
//        return LocalDate.parse(dateStr);
//    }
//}