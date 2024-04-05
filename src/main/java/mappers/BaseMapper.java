package mappers;

import models.DTO.BaseDTO;
import models.entities.BaseEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collection;

public interface BaseMapper<ENTITY extends BaseEntity, DTO extends BaseDTO> {

    DTO toDTO(ENTITY entity);

    ENTITY toEntity(DTO dto);

    Collection<DTO> toDTOs(Collection<ENTITY> entities);

    Collection<ENTITY> toEntities(Collection<DTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ENTITY updateEntity(DTO dto, @MappingTarget ENTITY entity);

}
