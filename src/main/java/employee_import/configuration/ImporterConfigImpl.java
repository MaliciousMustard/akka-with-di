package employee_import.configuration;

public class ImporterConfigImpl implements ImporterConfig {

    @Override
    public int getNumberOfThreads() {
        return 100;
    }
}
