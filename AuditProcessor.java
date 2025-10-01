public class AuditProcessor implements ItemProcessor<InputRecord, AuditRecord> {

    @Autowired private JdbcTemplate jdbcTemplate;

    @Override
    public AuditRecord process(InputRecord item) {
        String id = item.getID();
        String customValue = item.getCustomField();
        String dbStatus;

        try {
            dbStatus = jdbcTemplate.queryForObject(
                "SELECT status FROM your_table WHERE id = ?",
                new Object[]{id},
                String.class);
        } catch (EmptyResultDataAccessException e) {
            dbStatus = null;
        }

        String action = (dbStatus == null) ? "INSERT" :
                        (!dbStatus.equals(customValue)) ? "UPDATE" : "SKIP";

        AuditRecord audit = new AuditRecord();
        audit.setId(id);
        audit.setStatus(customValue);
        audit.setDbValue(dbStatus != null ? dbStatus : "NOT FOUND");
        audit.setAction(action);
        return audit;
    }
}
