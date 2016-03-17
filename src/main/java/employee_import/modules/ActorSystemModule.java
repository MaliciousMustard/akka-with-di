package employee_import.modules;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.Router;
import akka.routing.SmallestMailboxRoutingLogic;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import employee_import.configuration.ImporterConfig;
import employee_import.dao.EmployeeDao;
import employee_import.line_import.LineImportSupervisor;
import employee_import.line_import.LineImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ActorSystemModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActorSystemModule.class);

    @Override
    protected void configure() {
        bind(ActorSystem.class).toInstance(ActorSystem.apply());
    }

    @Provides
    private Router createLineImporterRouter(ActorSystem system, ImporterConfig config, EmployeeDao employeeDao) {
        return new Router(
                new SmallestMailboxRoutingLogic(),
                IntStream.range(0, config.getNumberOfThreads())
                        .mapToObj(i -> new ActorRefRoutee(system.actorOf(Props.create(LineImporter.class, employeeDao))))
                        .collect(toList())
        );
    }

    @Provides
    private ActorRef createLineImportSupervisor(final ActorSystem system, final Router router) {
        return system.actorOf(Props.create(LineImportSupervisor.class, () -> new LineImportSupervisor(router)));
    }
}
