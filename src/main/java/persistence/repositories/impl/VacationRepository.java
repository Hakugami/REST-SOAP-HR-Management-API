package persistence.repositories.impl;

import models.DTO.VacationDto;
import models.entities.Vacation;
import persistence.repositories.GenericRepository;

public class VacationRepository extends GenericRepository<Vacation, Long> {
    protected VacationRepository() {
        super(Vacation.class);
    }

    public static VacationRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        public static final VacationRepository INSTANCE = new VacationRepository();
    }
}
