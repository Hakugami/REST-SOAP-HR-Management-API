package mappers;

import models.DTO.BaseDTO;
import models.entities.BaseEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;

public interface BaseMapper<ENTITY extends BaseEntity, DTO extends BaseDTO> {

    default LocalDate map(java.util.Date date) {
        if (date instanceof java.sql.Date) {
            date = new java.util.Date(date.getTime());
        }
        return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    default java.util.Date map(LocalDate localDate) {
        return localDate != null ? java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    }

    DTO toDTO(ENTITY entity);

    ENTITY toEntity(DTO dto);

    Collection<DTO> toDTOs(Collection<ENTITY> entities);

    Collection<ENTITY> toEntities(Collection<DTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ENTITY updateEntity(DTO dto, @MappingTarget ENTITY entity);
}
