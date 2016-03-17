package employee_import.line_import;

import akka.actor.UntypedActor;
import employee_import.dao.EmployeeDao;
import employee_import.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LineImporter extends UntypedActor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LineImporter.class);

    private final EmployeeDao employeeDao;

    public LineImporter(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            boolean isSaved = processLine((String) message);
            getSender().tell(isSaved, getSelf());
        } else {
            LOGGER.warn("Found invalid message of type {}", message.getClass());
        }
    }

    private boolean processLine(String line) {
        String[] employeeDetails = line.split(",");
        if (employeeDetails.length != 2) {
            return false;
        }
        Employee employee = new Employee(employeeDetails[0], employeeDetails[1]);
        return employeeDao.save(employee);
    }
}
