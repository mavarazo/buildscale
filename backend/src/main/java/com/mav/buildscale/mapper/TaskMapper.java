package com.mav.buildscale.mapper;

import com.mav.buildscale.api.model.TaskDto;
import com.mav.buildscale.model.Task;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Arrays;

import static java.util.Objects.nonNull;

@Mapper(config = MapperConfig.class)
public interface TaskMapper {

    @Mapping(target = "report", ignore = true)
    @Mapping(target = "messages", source = ".")
    @Mapping(target = "incremental", source = "isIncremental")
    Task mapTaskDtoToTask(final TaskDto taskDto);

    default String mapMessagesToMessages(final TaskDto taskDto) {
        return nonNull(taskDto.getMessages())
                ? String.join("; ", taskDto.getMessages())
                : null;
    }

    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "isIncremental", source = "incremental")
    TaskDto mapTaskToTaskDto(final Task task);

    @AfterMapping
    default void afterMapping(@MappingTarget final TaskDto taskDto, final Task task) {
        taskDto.setMessages(nonNull(task.getMessages())
                ? Arrays.stream(task.getMessages().split("; ")).toList()
                : null);
    }

    default String map(final Task value) {
        return value.getMessages();
    }
}
