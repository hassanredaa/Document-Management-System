package com.app.dms.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Document(collection = "directories")
public class Directory {

    @Id
    private String id;
    private String name;
    private String parentId;
    private String workspaceId;
    private List<String> childDirectories = new ArrayList<>();
    private List<String> files = new ArrayList<>();
    private String path;
    private String createdBy;

    public void addFile(String file) {
        files.add(file);
    }

}
