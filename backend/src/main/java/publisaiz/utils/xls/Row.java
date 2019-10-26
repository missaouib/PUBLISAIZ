package publisaiz.utils.xls;

import java.util.HashMap;
import java.util.Map;

public final class Row {
    public Map<String, String> columns = new HashMap<>();

    public Row(Map<Integer, String> value, Map<Integer, String> columnNames) {
        value.entrySet().forEach(a -> map(a, columnNames));
    }

    private void map(Map.Entry<Integer, String> integerStringEntry, Map<Integer, String> columnNames) {
        columns.put(columnNames.get(integerStringEntry.getKey()), integerStringEntry.getValue());
    }

    public Map<String, String> getColumns() {
        return columns;
    }
}
