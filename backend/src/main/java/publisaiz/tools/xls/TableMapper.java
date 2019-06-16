package publisaiz.tools.xls;

import java.util.HashMap;
import java.util.Map;

class TableMapper {

    private Map<Integer, String> line = new HashMap<>();
    private Map<Integer, Map> sheet = new HashMap<>();

    TableMapper() {
    }

    public void appendCell(int index, String value) {
        line.put(index, value);
    }

    public void newRow() {
        line = new HashMap<>();
    }

    public void appendRow(int index) {
        sheet.put(index, line);
    }

    public Map<Integer, Map> getSheet() {
        return sheet;
    }
}
