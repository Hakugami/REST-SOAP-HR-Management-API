package services.impl;

import mappers.AttendanceMapper;
import models.DTO.AttendanceDto;
import models.entities.Attendance;
import persistence.repositories.impl.AttendanceRepository;
import services.BaseService;

public class AttendanceService extends BaseService<Attendance, AttendanceDto, Long> {

    protected AttendanceService() {
        super(AttendanceRepository.getInstance(), AttendanceMapper.INSTANCE);
    }

    public static AttendanceService getInstance() {
        return AttendanceService.SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        private static final AttendanceService INSTANCE = new AttendanceService();
    }
}
