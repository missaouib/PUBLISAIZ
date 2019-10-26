package publisaiz.utils.xls;

public class FileNotFoundException extends Exception {
    private static final long serialVersionUID = 448778329414189717L;

    @Override
    public String getMessage() {
        return "file format could not to be recognized as XLS or XLSX.";
    }
}
