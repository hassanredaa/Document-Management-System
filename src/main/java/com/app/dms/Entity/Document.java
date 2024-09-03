package com.app.dms.Entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@org.springframework.data.mongodb.core.mapping.Document(collection = "documents")
public class Document {

    @Id
    private String id;
    private String name;
    private String directoryId;
    private String ownerId;
    private String path;
    private boolean isDeleted;
}
