package dev.drawethree.licensesystem.controller;

import dev.drawethree.licensesystem.model.Software;
import dev.drawethree.licensesystem.service.SoftwareService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/software")
public class SoftwareController {


    private final SoftwareService softwareService;

    public SoftwareController(SoftwareService softwareService) {
        this.softwareService = softwareService;
    }


    @GetMapping("/list")
    public String viewSoftwares(Model model) {

        List<Software> softwareList = this.softwareService.findAll();

        model.addAttribute("softwares", softwareList);

        return "software/list-software";
    }

    @GetMapping("/create")
    public String createNewSoftware(Model model) {
        model.addAttribute("software", new Software());
        return "software/create-software";
    }

    @GetMapping("/update")
    public String updateEmployee(@RequestParam("softwareId") int id, Model model) {
        Software software = softwareService.getById(id);

        model.addAttribute("software", software);

        return "software/update-software";
    }

    @GetMapping("/delete")
    public String deleteSoftware(@RequestParam("softwareId") int id) {
        softwareService.deleteById(id);
        return "redirect:/software/list";
    }

    @PostMapping("/save")
    public String saveSoftware(@ModelAttribute("software") Software software) {
        software.setCreatedAt(LocalDateTime.now());
        softwareService.save(software);
        return "redirect:/software/list";
    }

    @GetMapping("/licenses")
    public String viewLicenses(@RequestParam("softwareId") int id, Model model) {
        Software software = softwareService.getById(id);

        model.addAttribute("software", software);

        return "license/list-license";
    }


}
