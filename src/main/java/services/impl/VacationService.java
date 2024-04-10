package services.impl;

import mappers.BaseMapper;
import mappers.VacationMapper;
import models.DTO.VacationDto;
import models.entities.Employee;
import models.entities.Vacation;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.GenericRepository;
import persistence.repositories.impl.EmployeeRepository;
import persistence.repositories.impl.VacationRepository;
import services.BaseService;

public class VacationService extends BaseService<Vacation, VacationDto,Long> {
    protected VacationService() {
        super(VacationRepository.getInstance(), VacationMapper.INSTANCE);
    }

    public static VacationService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public boolean requestVacation(String email , VacationDto vacationDto){
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Vacation vacation = mapper.toEntity(vacationDto);
            Employee employee = EmployeeRepository.getInstance().readByEmail(email, entityManager).orElse(null);
            if(employee == null){
                return false;
            }
            vacation.setIsApproved(false);
            vacation.setEmployee(employee);
            return repository.create(vacation, entityManager);
        });
    }

    public boolean approveVacation(Long vacationId){
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Vacation vacation = repository.read(vacationId, entityManager).orElse(null);
            if(vacation == null){
                return false;
            }
            vacation.setIsApproved(true);
            return repository.update(vacation, entityManager);
        });
    }

    public boolean rejectVacation(Long vacationId){
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Vacation vacation = repository.read(vacationId, entityManager).orElse(null);
            if(vacation == null){
                return false;
            }
            vacation.setIsApproved(false);
            return repository.update(vacation, entityManager);
        });
    }

    private static class SingletonHelper {
        public static final VacationService INSTANCE = new VacationService();
    }
}
