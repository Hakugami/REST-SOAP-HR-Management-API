package services;

import mappers.BaseMapper;
import models.DTO.BaseDTO;
import models.entities.BaseEntity;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.GenericRepository;
import persistence.repositories.helpers.filters.AbstractFilter;

import java.util.List;

public abstract class BaseService<ENTITY extends BaseEntity, DTO extends BaseDTO, ID> {
    private final GenericRepository<ENTITY, ID> repository;
    private final BaseMapper<ENTITY, DTO> mapper;

    protected BaseService(GenericRepository<ENTITY, ID> repository, BaseMapper<ENTITY, DTO> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public DTO save(DTO dto) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            ENTITY entity = mapper.toEntity(dto);
            repository.create(entity, entityManager);
            return mapper.toDTO(entity);
        });
    }


    public DTO read(ID id) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            ENTITY entity = repository.read(id, entityManager);
            return mapper.toDTO(entity);
        });
    }

    public boolean update(DTO dto, ID id) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            ENTITY entity = mapper.updateEntity(dto, repository.read(id, entityManager));
            return repository.update(entity, entityManager);
        });
    }


    public boolean delete(ID id) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> repository.delete(id, entityManager));
    }

    public <F extends AbstractFilter<ENTITY>> List<DTO> readAll(int offset, int limit, F filter) {
        return (List<DTO>) DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            List<ENTITY> entities = repository.readAll(entityManager, offset, limit, filter);
            return mapper.toDTOs(entities);
        });
    }

}
