package dev.drawethree.LicenseSystem.controller;

import dev.drawethree.LicenseSystem.model.Software;
import dev.drawethree.LicenseSystem.service.SoftwareService;
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

    @GetMapping("/")
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
    public String updateSoftware(@RequestParam("softwareId") int id, Model model) {
        Software software = softwareService.getById(id);

        model.addAttribute("software", software);

        return "software/update-software";
    }

    @GetMapping("/delete")
    public String deleteSoftware(@RequestParam("softwareId") int id) {
        softwareService.deleteById(id);
        return "redirect:/software/";
    }

    @PostMapping("/save")
    public String saveSoftware(@ModelAttribute("software") Software software) {
        software.setCreatedAt(LocalDateTime.now());
        softwareService.save(software);
        return "redirect:/software/";
    }

    @GetMapping("/licenses")
    public String viewLicenses(@RequestParam("softwareId") int id, Model model) {
        Software software = softwareService.getById(id);

        model.addAttribute("software", software);

        return "license/list-license";
    }


}
