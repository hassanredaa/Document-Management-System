package com.app.dms.Controller;


import com.app.dms.Entity.Workspace;
import com.app.dms.Requests.WorkspaceRequest;
import com.app.dms.Service.WorkspaceService;
import jakarta.servlet.http.HttpServletRequest;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.config.workspace.base-uri}")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @PostMapping("")
    public ResponseEntity<Workspace> createWorkspace(@RequestBody WorkspaceRequest workspaceRequest, Authentication authentication) {
        return workspaceService.createWorkspace(workspaceRequest.name,(String) authentication.getPrincipal());
    }

    @DeleteMapping("${app.config.workspace.api.delete-workspace-by-id}")
    public ResponseEntity deleteWorkspace(@PathVariable String id, Authentication authentication) {
        return workspaceService.deleteWorkspace(id, (String) authentication.getPrincipal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workspace> getWorkspace(@PathVariable String id) {
        return new ResponseEntity<Workspace>(workspaceService.getWorkspaceById(id), HttpStatus.OK);
    }



    @PutMapping("${app.config.workspace.api.update-workspace}/{workspace_id}")
    public ResponseEntity<Workspace> updateWorkspace(@RequestBody String name, @PathVariable String workspace_id , Authentication authentication) {
        return workspaceService.updateWorkspace(name,workspace_id,(String) authentication.getPrincipal());
    }

    @GetMapping("")
    public ResponseEntity<List<Workspace>> getUserWorkspaces(Authentication authentication) {
        return workspaceService.getAllWorkspaces((String) authentication.getPrincipal());
    }

}
