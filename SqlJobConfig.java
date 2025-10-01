@Configuration
@EnableBatchProcessing
public class SqlJobConfig {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private JdbcTemplate jdbcTemplate;

    @Bean
    public FlatFileItemReader<AuditRecord> auditReader() {
        return new FlatFileItemReaderBuilder<AuditRecord>()
            .name("auditReader")
            .resource(new FileSystemResource("audit_output.csv"))
            .delimited()
            .names("id", "status", "dbValue", "action")
            .targetType(AuditRecord.class)
            .linesToSkip(1)
            .build();
    }

    @Bean
    public AuditSqlProcessor sqlProcessor() {
        return new AuditSqlProcessor();
    }

    @Bean
    public SqlWriter sqlWriter() {
        return new SqlWriter("generated_queries.sql", jdbcTemplate, true);
    }

    @Bean
    public Job sqlJob(Step sqlStep) {
        return jobBuilderFactory.get("sqlJob")
            .start(sqlStep)
            .build();
    }

    @Bean
    public Step sqlStep(ItemReader<AuditRecord> reader,
                        ItemProcessor<AuditRecord, String> processor,
                        ItemWriter<String> writer) {
        return stepBuilderFactory.get("sqlStep")
            .<AuditRecord, String>chunk(10)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }
}
