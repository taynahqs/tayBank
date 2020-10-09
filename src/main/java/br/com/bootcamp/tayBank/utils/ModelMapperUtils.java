package br.com.bootcamp.tayBank.utils;

import org.modelmapper.ModelMapper;

public class ModelMapperUtils {

    public static Object convert(Object source, Class target) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, target);
    }

    private ModelMapperUtils() {

    }
}
