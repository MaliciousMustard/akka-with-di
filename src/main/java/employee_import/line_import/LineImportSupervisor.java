package employee_import.line_import;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import akka.routing.ActorRefRoutee;
import akka.routing.Router;
import akka.routing.SmallestMailboxRoutingLogic;
import employee_import.configuration.ImporterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class LineImportSupervisor extends UntypedActor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LineImportSupervisor.class);

    private Router router;
    private int pending = 0;

    @Inject
    public LineImportSupervisor(Creator<LineImporter> lineImporterCreator, ImporterConfig config) {
        this.router = new Router(
                new SmallestMailboxRoutingLogic(),
                IntStream.range(0, config.getNumberOfThreads())
                        .mapToObj(i -> new ActorRefRoutee(getContext().actorOf(Props.create(LineImporter.class, lineImporterCreator))))
                        .collect(toList())
        );
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof BufferedReader) {
            processImport((BufferedReader) message);
        } else if (message instanceof Boolean) {
            processImporterResponse();
        } else {
            LOGGER.warn("Found invalid message of type {}", message.getClass());
        }
    }

    private void processImport(BufferedReader reader) {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                router.route(line, getSelf());
                pending++;
            }
        } catch (IOException e) {
            LOGGER.error("Error while reading input", e);
        }
    }

    private void processImporterResponse() {
        pending--;
        if (pending == 0) {
            LOGGER.info("Import is completed. Shutting down actor system");
            getContext().system().terminate();
        }
    }

}
