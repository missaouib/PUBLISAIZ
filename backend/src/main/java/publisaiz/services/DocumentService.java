package publisaiz.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import publisaiz.datasources.database.entities.Document;
import publisaiz.datasources.database.repository.DocumentRepository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

@Service
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void save(Document document) {
        documentRepository.save(document);
    }

    public Page<Document> findAll(Pageable pageable) {
        return documentRepository.findAll(pageable);
    }

    public Document findById(Long id) {
        return documentRepository.findById(id).orElse(new Document());
    }
}