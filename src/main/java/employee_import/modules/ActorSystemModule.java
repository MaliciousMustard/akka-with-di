package employee_import.modules;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Creator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import employee_import.configuration.ImporterConfig;
import employee_import.dao.EmployeeDao;
import employee_import.line_import.LineImportSupervisor;
import employee_import.line_import.LineImporter;

public class ActorSystemModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ActorSystem.class).toInstance(ActorSystem.apply());
    }

    @Provides
    private ActorRef createLineImportSupervisor(final ActorSystem system, ImporterConfig config, EmployeeDao employeeDao) {
        Creator<LineImporter> lineImporterCreator = () -> new LineImporter(employeeDao);
        return system.actorOf(Props.create(LineImportSupervisor.class, lineImporterCreator, config));
    }
}
