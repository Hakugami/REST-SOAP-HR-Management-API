package mappers;

import models.DTO.AttendanceDto;
import models.entities.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance, AttendanceDto>{
    AttendanceMapper INSTANCE = Mappers.getMapper(AttendanceMapper.class);
}
