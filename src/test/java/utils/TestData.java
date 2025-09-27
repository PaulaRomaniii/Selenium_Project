package utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestData {
    private FormData formData;

    public FormData getFormData() {
        return formData;
    }

    public static TestData loadTestData(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), TestData.class);
    }
    public static class FormData {
        @JsonProperty("username")
        private String name;
        private String email;
        private String password;
        public String getName() {
            return name;
        }
        public String getEmail() {
            return email;
        }
        public String getPassword() {
            return password;
        }
        public Map<String, String> toMap() {
            Map<String, String> map = new HashMap<>();
            map.put("username", name);
            map.put("email", email);
            map.put("password", password);
            return map;
        }
    }
}
