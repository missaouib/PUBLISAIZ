package publisaiz.utils.xls;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
import java.util.stream.Collectors;

public class DataExtractorFacade {

    private final Logger logger = LoggerFactory.getLogger(DataExtractorFacade.class);
    private final Map<Integer, Map<Integer, Map<Integer, String>>> sheetList;

    public DataExtractorFacade() {
        sheetList = new HashMap<>();
    }

    public Map<Integer, Map<Integer, Map<Integer, String>>> getMap(MultipartFile multipartFile) throws FileNotFoundException, OpenXML4JException, SAXException, IOException {
        return process(multipartFile);
    }

    public Map<Integer, Table> getTables(MultipartFile file) throws FileNotFoundException, IOException, OpenXML4JException, SAXException {
        return makeTable(process(file));
    }

    private Map<Integer, Map<Integer, Map<Integer, String>>> process(MultipartFile file) throws IOException, FileNotFoundException, OpenXML4JException, SAXException {
        Props s = new Props(file).prepare();
        sheetList.clear();
        try {
            file.transferTo(s.getFileIo());
            s.getFileIo().setWritable(true);
            XlsProcessor xlsProcessor = new XlsProcessor();
            xlsProcessor.determineAndRunProcessing(s, sheetList);
        } finally {
            Files.delete(s.getPath());
        }
        return sheetList;
    }

    private Map<Integer, Table> makeTable(Map<Integer, Map<Integer, Map<Integer, String>>> sheetList) {
        return sheetList.entrySet().stream()
                .map(e -> new Table(e))
                .collect(Collectors.toMap(x -> x.getKey(), y -> y));
    }

    class Props {
        private MultipartFile file;
        private boolean isxls;
        private boolean xlsx;
        private Path path;
        private File fileIo;

        public Props(MultipartFile file) {
            this.file = file;
        }

        public boolean isIsxls() {
            return isxls;
        }

        public boolean isXlsx() {
            return xlsx;
        }

        public Path getPath() {
            return path;
        }

        public File getFileIo() {
            return fileIo;
        }

        public Props prepare() {
            isxls = isXLS(file);
            xlsx = isXLSX(file);
            path = Paths.get(file.getOriginalFilename());
            fileIo = new File(path.toUri());
            return this;
        }

        private boolean isXLS(MultipartFile file) {
            try {
                return POIFSFileSystem.hasPOIFSHeader(new BufferedInputStream(file.getInputStream()));
            } catch (IOException e) {
                return false;
            }
        }

        private boolean isXLSX(MultipartFile file) {
            try {
                return DocumentFactoryHelper.hasOOXMLHeader(file.getInputStream());
            } catch (IOException e) {
                logger.info("set as not xlsx but found IOExveption: {} ", e.getMessage());
                return false;
            }
        }
    }
}
