package employee_import.service;

import akka.actor.ActorRef;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;

@Singleton
public class EmployeeImportService {

    private final ActorRef lineImportSupervisor;

    @Inject
    public EmployeeImportService(ActorRef lineImportSupervisor) {
        this.lineImportSupervisor = lineImportSupervisor;
    }

    public void importEmployees(BufferedReader reader) {
        lineImportSupervisor.tell(reader, null);
    }

}
