package persistence.repositories.impl;

import lombok.extern.slf4j.Slf4j;
import models.entities.Attendance;
import persistence.repositories.GenericRepository;

@Slf4j
public class AttendanceRepository extends GenericRepository<Attendance, Long> {
    protected AttendanceRepository() {
        super(Attendance.class);
    }

    public static AttendanceRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        private static final AttendanceRepository INSTANCE = new AttendanceRepository();
    }

}
