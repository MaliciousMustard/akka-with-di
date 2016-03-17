package employee_import;

import com.google.inject.Guice;
import com.google.inject.Injector;
import employee_import.modules.ActorSystemModule;
import employee_import.modules.EmployeeImportModule;
import employee_import.service.EmployeeImportService;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class Main {

    private final EmployeeImportService employeeImportService;

    @Inject
    public Main(EmployeeImportService employeeImportService) {
        this.employeeImportService = employeeImportService;
    }

    public void process(String filename) throws FileNotFoundException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        employeeImportService.importEmployees(inputReader);
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            throw new IllegalArgumentException("You need to supply the name of the file you want to process");
        }

        Injector injector = Guice.createInjector(
                new EmployeeImportModule(),
                new ActorSystemModule()
        );
        Main mainObj = injector.getInstance(Main.class);
        mainObj.process(args[0]);
    }

}
