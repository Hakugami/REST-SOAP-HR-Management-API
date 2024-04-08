package mappers;

import models.DTO.AddressDto;
import models.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper extends BaseMapper<Address, AddressDto>{
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);
}
