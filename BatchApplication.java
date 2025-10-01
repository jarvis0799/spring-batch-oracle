@SpringBootApplication
public class BatchApplication implements CommandLineRunner {

    @Autowired private JobLauncher jobLauncher;
    @Autowired private Job auditJob;
    @Autowired private Job sqlJob;
    @Value("${spring.profiles.active}") private String activeProfile;

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .toJobParameters();

        if ("audit".equals(activeProfile)) {
            jobLauncher.run(auditJob, params);
        } else if ("sql".equals(activeProfile)) {
            jobLauncher.run(sqlJob, params);
        } else {
            System.out.println("Unknown profile: " + activeProfile);
        }
    }
}
