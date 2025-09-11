package com.k_passs.backend.domain.ocr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NaverOcrResponseDto {

    private String version;
    private String requestId;
    private long timestamp;
    private List<Image> images;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Image {
        private String uid;
        private String name;
        private String inferResult;
        private String message;
        private List<Field> fields;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Field {
        private String valueType;
        private BoundingPoly boundingPoly;
        private String inferText;
        private double inferConfidence;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class BoundingPoly {
        private List<Vertex> vertices;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Vertex {
        private double x;
        private double y;
    }
}