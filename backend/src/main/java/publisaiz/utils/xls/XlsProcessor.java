package publisaiz.utils.xls;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

class XlsProcessor {

    private Logger logger = LoggerFactory.getLogger(XlsProcessor.class);

    void determineAndRunProcessing(DataExtractorFacade.Props s, Map<Integer, Map<Integer, Map<Integer, String>>> sheetList) throws FileNotFoundException, IOException, OpenXML4JException, SAXException {
        if (!s.isIsxls() && !s.isXlsx()) {
            throw new FileNotFoundException();
        } else if (s.isXlsx()) {
            processXlsx(s, sheetList);
        } else {
            processXls(s, sheetList);
        }
    }

    void processXlsx(DataExtractorFacade.Props s, Map<Integer, Map<Integer, Map<Integer, String>>> sheetList) throws IOException, OpenXML4JException, SAXException {
        XlsToolkit xls = new XlsToolkit(s.getFileIo());
        xls.process().entrySet().forEach(e -> sheetList.put(e.getKey(), e.getValue()));
    }

    void processXls(DataExtractorFacade.Props props, Map<Integer, Map<Integer, Map<Integer, String>>> sheetList) throws IOException, InvalidFormatException {
        try (Workbook wb = WorkbookFactory.create(props.getFileIo())) {
            Files.delete(props.getFileIo().toPath());
            int sheets = wb.getNumberOfSheets();
            processSheets(sheetList, wb, sheets);
        }
    }

    void processSheets(Map<Integer, Map<Integer, Map<Integer, String>>> sheetList, Workbook wb, int sheets) {
        for (int i = 0; i < sheets; i++) {
            Sheet mySheet = wb.getSheetAt(i);
            mySheet.setDisplayFormulas(true);
            Map recentsheet = new HashMap();
            processRows(mySheet, recentsheet);
            sheetList.put(i, recentsheet);
        }
    }

    void processRows(Sheet mySheet, Map recentsheet) {
        for (Row row : mySheet) {
            Map rowList = new HashMap();
            processCell(row, rowList);
            recentsheet.put(row.getRowNum(), rowList);
        }
    }

    void processCell(Row row, Map rowList) {
        for (Cell cell : row) {
            String val;
            CellType type = cell.getCellTypeEnum();
            val = resolveTypeOfCell(cell, type);
            rowList.put(cell.getColumnIndex(), val);
        }
    }

    String resolveTypeOfCell(Cell cell, CellType type) {
        String val;
        try {
            switch (type) {
                case BOOLEAN:
                    val = String.valueOf(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    val = String.valueOf(cell.getNumericCellValue());
                    break;
                case STRING:
                    val = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    val = String.valueOf(cell.getNumericCellValue());
                    break;
                default:
                    val = "could not interpret the cell content";
                    break;
            }
        } catch (Exception e) {
            logger.info("resolveTypeOfCell founf exception {}", e.getMessage());
            val = "error";
        }
        return val;
    }
}