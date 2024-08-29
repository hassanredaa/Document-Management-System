package com.app.dms.Requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectoryRequest {
    public String name;
    public String parentId;
    public String workspaceId;
}