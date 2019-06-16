package publisaiz.executors;

import org.springframework.stereotype.Component;
import publisaiz.datasources.database.entities.Document;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.entities.Warning;
import publisaiz.services.DocumentService;
import publisaiz.services.WarningService;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

@Component
public class XlsPersistationExecutor {

    public XlsPersistationExecutor() {
    }

    public void process(WarningService warningService, DocumentService documentService, Map<Integer, Map> res, User loggedUser, String documentName) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.execute(() -> {
            for (int i = 0; i < res.size(); i++) {
                Map sheet = res.get(i);
                Collection<String> keyscollection = null;
                int rowNumber = 0;
                while (keyscollection == null) {
                    Map<Integer, String> keyset = (Map<Integer, String>) sheet.get(rowNumber);
                    if (keyset != null && !keyset.isEmpty())
                        keyscollection = keyset.values();
                    else
                        rowNumber++;
                }
                int intkey = 0;
                String[] keys = new String[keyscollection.size()];
                for (String key : keyscollection) {
                    keys[intkey++] = key;
                }
                for (; rowNumber < sheet.size(); rowNumber++) {
                    Map<Integer, String> row = (Map<Integer, String>) sheet.get(rowNumber);
                    if (row == null || row.isEmpty())
                        rowNumber++;
                    else
                        for (int k = 0; k < row.size(); k++) {
                            Document document = new Document();
                            if (keys.length > k) {
                                document.setKey(keys[k]);
                            }
                            String data;
                            try {
                                data = row.get(k);
                                if (data != null && data.length() > 0) {
                                    document.setValue(data);
                                    document.setRow(rowNumber);
                                    document.setDate(ZonedDateTime.now());
                                    document.setUser(loggedUser);
                                    document.setDocumentName(documentName + " sheet: " + i);
                                    documentService.save(document);
                                }
                            } catch (Exception e) {
                                warningService.save(new Warning("PROBLEM WITH SAVING DOCUMENT " + documentName + " ROW " + rowNumber + " FIELD " + k, loggedUser));
                            }
                        }
                }
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    Logger.getLogger("XLS PARSER").log(Level.SEVERE, e.getLocalizedMessage());
                }
            }
        });
    }
}
