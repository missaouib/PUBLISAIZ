package publisaiz.tools.xls;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class XlsHandleProcessing {

    Logger logger = LoggerFactory.getLogger(XlsHandleProcessing.class);

    private boolean isXLSX(MultipartFile file) {
        boolean xlsx = false;
        try {
            xlsx = DocumentFactoryHelper.hasOOXMLHeader(file.getInputStream());
        } catch (IOException e) {
            logger.warn("set as not xlsx but found IOExveption: {} ", e.getMessage());
        }
        return xlsx;
    }

    private boolean isXLS(MultipartFile file) {
        boolean isxls = false;
        try {
            isxls = POIFSFileSystem.hasPOIFSHeader(new BufferedInputStream(file.getInputStream()));
        } catch (IOException e) {
            isxls = false;
        }
        return isxls;
    }

    public Map<Integer, Map> handleprocessing(MultipartFile file) throws FileNotFoundException, IOException, OpenXML4JException, SAXException {
        boolean isxls = isXLS(file);
        boolean xlsx = isXLSX(file);
        Path path = Paths.get(file.getOriginalFilename());
        File fileIo = new File(path.toUri());
        Map<Integer, Map> sheetList = new HashMap<>();
        try {
            file.transferTo(fileIo);
            fileIo.setWritable(true);
            if (!isxls && !xlsx) {
                throw new FileNotFoundException();
            } else if (xlsx) {
                XlsToolkit xls = new XlsToolkit(fileIo);
                sheetList = xls.process();
            } else {
                processXls(fileIo, sheetList);
            }
        } finally {
            Files.delete(path);
        }
        return sheetList;
    }

    private void processXls(File fileIo, Map<Integer, Map> sheetList) throws IOException, InvalidFormatException {
        try (Workbook wb = WorkbookFactory.create(fileIo)) {
            Files.delete(fileIo.toPath());
            int sheets = wb.getNumberOfSheets();
            processSheets(sheetList, wb, sheets);
        }
    }

    private void processSheets(Map<Integer, Map> sheetList, Workbook wb, int sheets) {
        for (int i = 0; i < sheets; i++) {
            Sheet mySheet = wb.getSheetAt(i);
            mySheet.setDisplayFormulas(true);
            Map recentsheet = new HashMap();
            processRows(mySheet, recentsheet);
            sheetList.put(i, recentsheet);
        }
    }

    private void processRows(Sheet mySheet, Map recentsheet) {
        for (Row row : mySheet) {
            Map rowList = new HashMap();
            processCell(row, rowList);
            recentsheet.put(row.getRowNum(), rowList);
        }
    }

    private void processCell(Row row, Map rowList) {
        for (Cell cell : row) {
            String val = "";
            CellType type = cell.getCellTypeEnum();
            val = resolveTypeOfCell(cell, type);
            rowList.put(cell.getColumnIndex(), val);
        }
    }

    private String resolveTypeOfCell(Cell cell, CellType type) {
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
            logger.warn("resolveTypeOfCell founf exception {}", e.getMessage());
            val = "error";
        }
        return val;
    }
}
