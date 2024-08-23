package com.app.dms.Controller;


import com.app.dms.Entity.Workspace;
import com.app.dms.Service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @PostMapping("${endpoints.createworkspace}")
    public ResponseEntity<Workspace> createWorkspace(@RequestBody Workspace workspace) {
        return workspaceService.createWorkspace(workspace);
    }

    @DeleteMapping("${endpoints.deleteworkspace}/{id}")
    public ResponseEntity deleteWorkspace(@PathVariable String id) {
        return workspaceService.deleteWorkspace(id);
    }

    @GetMapping("${endpoints.getworkspace}/{id}")
    public ResponseEntity<Workspace> getWorkspace(@PathVariable String id) {
        return workspaceService.getWorkspaceById(id);
    }

    @PutMapping("${endpoints.updateworkspace}")
    public ResponseEntity<Workspace> updateWorkspace(@RequestBody Workspace workspace) {
        return workspaceService.updateWorkspace(workspace);
    }

}
