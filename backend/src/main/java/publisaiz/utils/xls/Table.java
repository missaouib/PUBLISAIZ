package publisaiz.utils.xls;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Table {

    private final Integer key;
    private Map<Integer, Row> rows = new HashMap<>();
    private Map<Integer, String> columnNames = null;

    public Table(Map.Entry<Integer, Map<Integer, Map<Integer, String>>> e) {
        key = e.getKey();
        Map<Integer, Map<Integer, String>> value = e.getValue();
        Set<Map.Entry<Integer, Map<Integer, String>>> entry = value.entrySet();
        entry.forEach(en -> addRow(en));
    }

    private void addRow(Map.Entry<Integer, Map<Integer, String>> entries) {
        if (columnNames == null) {
            columnNames = entries.getValue();
        }
        rows.put(entries.getKey(), new Row(entries.getValue(), columnNames));
    }

    public Integer getKey() {
        return key;
    }

    public Map<Integer, String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Map<Integer, String> columnNames) {
        this.columnNames = columnNames;
    }

    public Map<Integer, Row> getRows() {
        return rows;
    }

    public void setRows(Map<Integer, Row> rows) {
        this.rows = rows;
    }

    public String getColumnName(Integer key) {
        return columnNames.get(key);
    }
}
