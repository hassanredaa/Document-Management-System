package com.app.dms.Repository;

import com.app.dms.Entity.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<Document, String> {
}
