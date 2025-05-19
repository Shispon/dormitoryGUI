package org.diplom.dormitory.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.diplom.dormitory.model.ParentDTO;
import org.diplom.dormitory.model.ResidentDTO;
import org.diplom.dormitory.model.ResidentParentModel;
import org.diplom.dormitory.model.StaffDTO;

public class JsonBuilder {

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    // ObjectMapper с поддержкой LocalDateTime
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String buildResidentJson(ResidentDTO dto) throws JsonProcessingException {
        dto.setRoleId(2);
        return objectMapper.writeValueAsString(dto);
    }

    public static String buildParentJson(ParentDTO parent) throws JsonProcessingException {
        parent.setRoleId(3);
        return objectMapper.writeValueAsString(parent);
    }

    public static String buildResidentParentJson(ResidentParentModel residentParentModel) throws JsonProcessingException {
        return objectMapper.writeValueAsString(residentParentModel);
    }

    public static String buildStaffJson(StaffDTO staffDTO) throws JsonProcessingException {
        return objectMapper.writeValueAsString(staffDTO);
    }
}
