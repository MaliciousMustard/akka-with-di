package employee_import.modules;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import employee_import.configuration.ImporterConfig;
import employee_import.dao.EmployeeDao;
import employee_import.line_import.LineImportSupervisor;
import employee_import.line_import.LineImporter;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ActorSystemModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ActorSystem.class).toInstance(ActorSystem.apply());
    }

    @Provides
    private ActorRef createLineImportSupervisor(final ActorSystem system, ImporterConfig config, EmployeeDao employeeDao) {
        List<Props> lineImporterProps = IntStream.range(0, config.getNumberOfThreads())
                .mapToObj(i -> Props.create(LineImporter.class, () -> new LineImporter(employeeDao))).collect(toList());

        return system.actorOf(Props.create(LineImportSupervisor.class, () -> {
            return new LineImportSupervisor(lineImporterProps);
        }));
    }
}
