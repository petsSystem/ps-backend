package br.com.petshop.customer.service;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
public class GeometryService {
    private GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);

    public Point getPoint(Double lat, Double lon){
        return factory.createPoint(new Coordinate(lon, lat));
    }
}
