package ar.edu.unq.desapp.grupoG.backenddesappapi.controller;

import ar.edu.unq.desapp.grupoG.backenddesappapi.aspects.AuditLogger;
import ar.edu.unq.desapp.grupoG.backenddesappapi.exceptions.MissingDataException;
import ar.edu.unq.desapp.grupoG.backenddesappapi.model.Project;
import ar.edu.unq.desapp.grupoG.backenddesappapi.service.ProjectService;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;

@RestController
@RequestMapping("projects")
@EnableAutoConfiguration
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping(path = "/project/{id}")
    @ResponseBody
    public Project getProjectById(@PathVariable Integer id) {
        return projectService.findById(id);
    }
    
    @AuditLogger
    @PostMapping(path = "/close")
    public void closeProject(@RequestBody Project project) throws MessagingException {
        try{
            projectService.closeProject(project.getIdProject());
        }catch (MessagingException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "HAYQ UE CAMBIAR ESTO AAAAAA", e);
        }
    }

    @AuditLogger
    @GetMapping(path = "/allOpenProjects")
    @ResponseBody
    public Stream<Project> getOpenProjects(@PageableDefault(size = 5, page = 0) Pageable page) {
        return projectService.findOpenProjects(page).get();
    }

    @GetMapping(path = "/projectCloseToFinish")
    @ResponseBody
    public Stream<Project> findProjectsCloseToFinish(@PageableDefault(size = 5, page = 0) Pageable page) {
        return projectService.findProjectsCloseToFinish(page).get();
    }

    @AuditLogger
    @PostMapping(path = "/create")
    public void createProject(@RequestBody Project project) throws Exception {
        try {
    	    projectService.save(project);
        } catch (MissingDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan datos del proyecto", e);
        }
    }

    @DeleteMapping("/project/{id}")
    public void deleteEmployee(@PathVariable Integer id) {
        projectService.deleteById(id);
    }

}