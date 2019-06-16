package publisaiz.tools.xls;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XlsToolkit {

    private TableMapper output;
    private OPCPackage xlsxPackage;
    private String sheetName;

    public XlsToolkit(File xlsxFile) {
        output = new TableMapper();
        if (!xlsxFile.exists()) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Not found or not a file: " + xlsxFile.getPath());
            return;
        }
        OPCPackage pkg = null;
        try {
            pkg = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        this.xlsxPackage = pkg;
    }

    public void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetHandler,
                             InputStream sheetInputStream) {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }
    }

    public Map<Integer, Map> process() throws IOException, OpenXML4JException, SAXException {
        Map<Integer, Map> res = new HashMap<>();
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            sheetName = iter.getSheetName();
            processSheet(styles, strings, new Harvester(), stream);
            stream.close();
            Map<Integer, Map> sheet = output.getSheet();
            res.put(index, sheet);
            ++index;
        }
        return res;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    private class Harvester implements SheetContentsHandler {

        @Override
        public void startRow(int rowNum) {
            output.newRow();
        }

        @Override
        public void endRow(int rowNum) {
            output.appendRow(rowNum);
        }

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {
            int thisCol = (new CellReference(cellReference)).getCol();
            output.appendCell(thisCol, formattedValue);
        }


        @Override
        public void headerFooter(String s, boolean b, String s1) {
            // posible implementation ;)
        }
    }
}
