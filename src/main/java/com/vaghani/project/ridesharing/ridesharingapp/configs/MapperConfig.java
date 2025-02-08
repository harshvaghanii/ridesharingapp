package com.vaghani.project.ridesharing.ridesharingapp.configs;

import com.vaghani.project.ridesharing.ridesharingapp.dto.PointDto;
import com.vaghani.project.ridesharing.ridesharingapp.utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.typeMap(PointDto.class, Point.class).setConverter(converter -> {
            PointDto pointDTO = converter.getSource();
            return GeometryUtil.createPoint(pointDTO);
        });

        mapper.typeMap(Point.class, PointDto.class).setConverter(context -> {
            Point point = context.getSource();
            double[] coordinates = {
                    point.getX(),
                    point.getY()
            };
            return new PointDto(coordinates);
        });

        return mapper;
    }

}
