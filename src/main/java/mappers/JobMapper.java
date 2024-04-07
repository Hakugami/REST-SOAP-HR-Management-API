package mappers;

import models.DTO.JobDto;
import models.entities.Job;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobMapper extends BaseMapper<Job, JobDto>{
    JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);
}
