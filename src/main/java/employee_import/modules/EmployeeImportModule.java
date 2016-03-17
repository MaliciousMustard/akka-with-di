package employee_import.modules;

import com.google.inject.AbstractModule;
import employee_import.configuration.ImporterConfig;
import employee_import.configuration.ImporterConfigImpl;
import employee_import.dao.EmployeeDao;
import employee_import.dao.EmployeeLoggerDao;

public class EmployeeImportModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EmployeeDao.class).to(EmployeeLoggerDao.class);
        bind(ImporterConfig.class).to(ImporterConfigImpl.class);
    }

}
