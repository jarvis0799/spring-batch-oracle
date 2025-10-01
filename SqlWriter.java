public class SqlWriter implements ItemWriter<String> {

    private final String outputPath;
    private final JdbcTemplate jdbcTemplate;
    private final boolean executeSql;

    public SqlWriter(String outputPath, JdbcTemplate jdbcTemplate, boolean executeSql) {
        this.outputPath = outputPath;
        this.jdbcTemplate = jdbcTemplate;
        this.executeSql = executeSql;
    }

    @Override
    public void write(List<? extends String> items) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, true))) {
            for (String sql : items) {
                if (sql != null) {
                    writer.write(sql);
                    writer.newLine();
                    if (executeSql) {
                        try {
                            jdbcTemplate.execute(sql);
                        } catch (DataAccessException e) {
                            System.err.println("SQL execution failed: " + sql);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
