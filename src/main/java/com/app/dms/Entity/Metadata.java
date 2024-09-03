package com.app.dms.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
public class Metadata {
        @Id
        private String documentId;
        private String documentType;
        private String ownerFirstName;
        private String ownerLastName;
        private int version;
        private String tag;
    }
