package publisaiz.tools.xls;

public class FileNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "file format could not to be recognized as XLS or XLSX.";
    }
}
