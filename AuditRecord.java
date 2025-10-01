public class AuditRecord {
    private String id;
    private String status;
    private String dbValue;
    private String action;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDbValue() { return dbValue; }
    public void setDbValue(String dbValue) { this.dbValue = dbValue; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
