package com.sling.web.search.mapper;

import com.sling.model.search.Search;
import com.sling.web.search.dto.SearchRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(imports = {LocalDate.class, List.class})
public interface SearchMapper {
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    SearchMapper MAPPER = Mappers.getMapper(SearchMapper.class);

    @Mapping(target = "ages", expression = "java(List.copyOf(dto.ages()))")
    @Mapping(target = "checkIn", expression = "java(LocalDate.parse(dto.checkIn(), FORMATTER).toString())")
    @Mapping(target = "checkOut", expression = "java(LocalDate.parse(dto.checkOut(), FORMATTER).toString())")
    Search toDomain(SearchRequestDto dto);
}
