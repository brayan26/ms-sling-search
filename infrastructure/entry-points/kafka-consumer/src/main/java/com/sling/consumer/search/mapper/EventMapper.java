package com.sling.consumer.search.mapper;

import com.sling.consumer.search.dto.SearchEvent;
import com.sling.model.search.Search;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(imports = {List.class})
public interface EventMapper {
    EventMapper MAPPER = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "ages", expression = "java(List.copyOf(searchEvent.ages()))")
    Search toDomain(SearchEvent searchEvent);
}
