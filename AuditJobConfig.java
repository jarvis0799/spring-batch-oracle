@Configuration
@EnableBatchProcessing
public class AuditJobConfig {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<InputRecord> reader() {
        return new FlatFileItemReaderBuilder<InputRecord>()
            .name("csvReader")
            .resource(new FileSystemResource("input.csv"))
            .delimited()
            .names("ID", "CustomField")
            .targetType(InputRecord.class)
            .build();
    }

    @Bean
    public ItemProcessor<InputRecord, AuditRecord> processor() {
        return new AuditProcessor();
    }

    @Bean
    public FlatFileItemWriter<AuditRecord> writer() {
        return new FlatFileItemWriterBuilder<AuditRecord>()
            .name("auditWriter")
            .resource(new FileSystemResource("audit_output.csv"))
            .delimited()
            .delimiter(",")
            .names("id", "status", "dbValue", "action")
            .build();
    }

    @Bean
    public Job auditJob(Step step1) {
        return jobBuilderFactory.get("auditJob")
            .start(step1)
            .build();
    }

    @Bean
    public Step step1(ItemReader<InputRecord> reader,
                      ItemProcessor<InputRecord, AuditRecord> processor,
                      ItemWriter<AuditRecord> writer) {
        return stepBuilderFactory.get("step1")
            .<InputRecord, AuditRecord>chunk(10)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }
}
