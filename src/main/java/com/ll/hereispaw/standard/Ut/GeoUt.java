package com.ll.hereispaw.standard.Ut;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeoUt {
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 위도와 경도로 Point 객체 생성
     * @param longitude 경도
     * @param latitude 위도
     * @return Point 객체
     */
    public static Point createPoint(double longitude, double latitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    /**
     * "POINT(경도 위도)" 형식의 WKT 문자열에서 Point 객체 생성
     * @param wktPoint WKT 형식의 포인트 문자열
     * @return Point 객체
     */
    public static Point wktToPoint(String wktPoint) {
        try {
            // JSON 문자열을 JsonNode로 파싱
            JsonNode jsonNode = objectMapper.readTree(wktPoint);

            // x와 y 값 추출
            double longitude = jsonNode.get("x").asDouble();
            double latitude = jsonNode.get("y").asDouble();

            return createPoint(longitude, latitude);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}