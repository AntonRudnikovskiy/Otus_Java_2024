package otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import otus.model.Measurement;

import java.io.IOException;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        ClassLoader classLoader = getClass().getClassLoader();
        List<Measurement> measurements;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        try {
            measurements = objectMapper.readValue(classLoader.getResourceAsStream(getFileName()), new TypeReference<>() {});
        } catch (IOException ex) {
            throw new FileProcessException(ex);
        }
        return measurements;
    }

    public String getFileName() {
        return fileName;
    }
}
