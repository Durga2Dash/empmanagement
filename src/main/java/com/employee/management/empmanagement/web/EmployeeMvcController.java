package com.employee.management.empmanagement.web;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.employee.management.empmanagement.exception.RecordNotFoundException;
import com.employee.management.empmanagement.model.EmployeeEntity;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
public class EmployeeMvcController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping
    public String getAllEmployees(Model model) {
        List<EmployeeEntity> list = (List<EmployeeEntity>) restTemplate.getForObject("http://localhost:8011/data", List.class);
        System.out.println("[getAllEmployees] response : " + list);
        model.addAttribute("employees", list);
        return "list-employees";
    }

    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String editEmployeeById(Model model, @PathVariable("id") Optional<Long> id)
            throws RecordNotFoundException {
        if (id.isPresent()) {
            System.out.println("[editEmployeeById] Id : "+id);
            ResponseEntity<EmployeeEntity> entity = restTemplate.getForEntity("http://localhost:8011/data/edit/" + id.get(), EmployeeEntity.class);
            System.out.println("[editEmployeeById] response : " + entity.getBody() + " , status code : " + entity.getStatusCode());
            model.addAttribute("employee", entity.getBody());
        } else {
            model.addAttribute("employee", new EmployeeEntity());
        }
        return "add-edit-employee";
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteEmployeeById(Model model, @PathVariable("id") Long id)
            throws RecordNotFoundException {
        System.out.println("[deleteEmployeeById] Id : "+id);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> reqEntity = new HttpEntity<String>(headers);
        ResponseEntity<String> resEntity = restTemplate.exchange("http://localhost:8011/data/delete/" + id, HttpMethod.DELETE, reqEntity, String.class);
        System.out.println("[deleteEmployeeById] response : " + resEntity.getBody() + " , status code : " + resEntity.getStatusCode());
        return "redirect:/";
    }

    @RequestMapping(path = "/createEmployee", method = RequestMethod.POST)
    public String createOrUpdateEmployee(EmployeeEntity employee) {
        System.out.println("[createOrUpdateEmployee] employee : "+employee);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<EmployeeEntity> reqEntity = new HttpEntity<EmployeeEntity>(employee, headers);
        //ResponseEntity<String> resEntity = restTemplate.exchange("http://localhost:8011/data/create-update", HttpMethod.POST, reqEntity, String.class);


        ResponseEntity<String> resEntity = restTemplate.postForEntity(
               "http://localhost:8011/data/create-update", reqEntity , String.class);

        System.out.println("[createOrUpdateEmployee] response : " + resEntity.getBody() + " , status code : " + resEntity.getStatusCode());
        return "redirect:/";
    }
}
