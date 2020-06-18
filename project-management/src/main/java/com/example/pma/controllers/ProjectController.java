package com.example.pma.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pma.dao.EmployeeRepository;
import com.example.pma.dao.ProjectRepository;
import com.example.pma.entities.Employee;
import com.example.pma.entities.Project;

@Controller
@RequestMapping("/projects")
public class ProjectController {
	
	@Autowired
	ProjectRepository proRepo; 
	// autowired means giving responsibility to spring to inject an instance of ProjectRepository 
	// using an anonymous class since this is an interface
	
	@Autowired
	EmployeeRepository empRepo;
	
	@GetMapping
	public String displayProjects(Model model) {
		
		List<Project> projectList = proRepo.findAll();
		model.addAttribute("projectsList", projectList);
		return "projects/list-projects";
	}

	@GetMapping("/new")
	public String displayProjectForm(Model model) { // model is used to exchange data between the view and the controller
		
		Project aProject = new Project();
		List<Employee> empList = empRepo.findAll();
		model.addAttribute("allEmployees", empList);
		model.addAttribute("project", aProject);
		return "projects/new-project";
	}
	
	@PostMapping("/save")
	public String createProject(Project project, BindingResult bindingResult, @RequestParam List<Long> employees, Model model) {
		
		proRepo.save(project);
		
		Iterable<Employee> chosenEmployees = empRepo.findAllById(employees);
		for(Employee emp : chosenEmployees) {
			emp.setProject(project);
			empRepo.save(emp);
		}
		
		// use a redirect to prevent duplicate submissions
		return "redirect:/projects/new";
	}
}
