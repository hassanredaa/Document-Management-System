package com.app.dms.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectoryRequest {
    public String name;
    public String parentId;
    public String workspaceId;
}