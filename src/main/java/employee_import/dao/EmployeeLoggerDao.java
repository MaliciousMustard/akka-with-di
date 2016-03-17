package employee_import.dao;

import employee_import.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeLoggerDao implements EmployeeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeLoggerDao.class);

    @Override
    public boolean save(Employee employee) {
        LOGGER.info("{} is working as {}", employee.getName(), employee.getPosition());
        return true;
    }
}
