public class AuditSqlProcessor implements ItemProcessor<AuditRecord, String> {

    @Override
    public String process(AuditRecord record) {
        String id = record.getId();
        String status = record.getStatus();
        String action = record.getAction();

        return switch (action) {
            case "INSERT" -> String.format("INSERT INTO your_table (id, status) VALUES ('%s', '%s');", id, status);
            case "UPDATE" -> String.format("UPDATE your_table SET status = '%s' WHERE id = '%s';", status, id);
            default -> null;
        };
    }
}
