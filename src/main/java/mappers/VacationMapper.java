package mappers;

import models.DTO.VacationDto;
import models.entities.Vacation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VacationMapper extends BaseMapper<Vacation, VacationDto> {
    VacationMapper INSTANCE = Mappers.getMapper(VacationMapper.class);
}
