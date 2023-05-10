package com.mav.buildscale.mapper;

import com.mav.buildscale.api.model.TagDto;
import com.mav.buildscale.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface TagMapper {

    @Mapping(target = "report", ignore = true)
    Tag mapTagDtoToTag(final TagDto tagDto);

    TagDto mapTagToTagDto(Tag tag);
}
