package dev.drawethree.LicenseSystem.software.controller;

import dev.drawethree.LicenseSystem.license.model.License;
import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.software.service.SoftwareService;
import dev.drawethree.LicenseSystem.software.validation.SoftwareValidator;
import dev.drawethree.LicenseSystem.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("software")
public class SoftwareController {

    private final SoftwareService softwareService;

    private final SecurityService securityService;

    private final SoftwareValidator softwareValidator;

    public SoftwareController(SoftwareService softwareService, SecurityService securityService, SoftwareValidator softwareValidator) {
        this.softwareService = softwareService;
        this.securityService = securityService;
        this.softwareValidator = softwareValidator;
    }

    @GetMapping
    public String showIndex(Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = securityService.getCurrentUser();

        List<Software> softwareList = this.softwareService.findAllByCreator(user);

        model.addAttribute("softwares", softwareList);

        return "software/index";
    }

    @GetMapping("/create")
    public String showCreateNewSoftwareForm(Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        model.addAttribute("software", new Software());
        return "software/create";
    }

    @GetMapping("/list")
    public String showPublicSoftwares(@RequestParam("page") Optional<Integer> page, Model model) {

        int currentPage = page.orElse(1);
        int pageSize = 5;

        Page<Software> softwarePage = softwareService.findPaginated(PageRequest.of(currentPage - 1, pageSize), true);

        int totalPages = softwarePage.getTotalPages();

        if (currentPage > totalPages && totalPages > 0) {
            return "redirect:/software/list";
        }

        model.addAttribute("software_page", softwarePage);

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }

        return "software/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateSoftwareForm(@PathVariable("id") int id, Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        Optional<Software> software = softwareService.findById(id);

        if (software.isEmpty()) {
            return "software/index";
        }

        model.addAttribute("software", software.get());

        return "software/update";
    }

    @PostMapping("/update/{id}")
    public String updateSoftware(@PathVariable("id") int id, @ModelAttribute("software") Software tempSoftware, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        softwareValidator.validate(tempSoftware, bindingResult);

        if (bindingResult.hasErrors()) {
            return "software/update";
        }

        Optional<Software> optionalSoftware = softwareService.findById(id);

        if (optionalSoftware.isEmpty()) {
            return "software/index";
        }

        Software software = optionalSoftware.get();
        software.setName(tempSoftware.getName());
        software.setDescription(tempSoftware.getDescription());
        software.setVisible(tempSoftware.isVisible());

        softwareService.save(software);

        redirectAttributes.addFlashAttribute("success", "Successfully updated software!");

        return "redirect:/software";
    }

    @GetMapping("/delete/{id}")
    public String deleteSoftware(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        User currentUser = securityService.getCurrentUser();
        Optional<Software> softwareOptional = softwareService.findById(id);

        if (softwareOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Software does not exist.");
            return "redirect:/software";
        }

        if (!softwareOptional.get().getCreator().equals(currentUser) && !currentUser.isAdmin()) {
            redirectAttributes.addFlashAttribute("error", "Not authorized.");
            return "redirect:/software";
        }

        softwareService.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Successfully deleted software!");

        return "redirect:/software";
    }

    @PostMapping("/create")
    public String createSoftware(@ModelAttribute("software") Software software, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        softwareValidator.validate(software, bindingResult);

        if (bindingResult.hasErrors()) {
            return "software/create";
        }

        User user = securityService.getCurrentUser();

        if (!user.canCreateSoftware()) {
            redirectAttributes.addFlashAttribute("error", "You have reached maximum amount of software for your account!");
            return "redirect:/software";
        }

        software.setCreator(user);
        software.setCreatedAt(LocalDateTime.now());

        softwareService.save(software);

        redirectAttributes.addFlashAttribute("success", "Successfully create new software!");

        return "redirect:/software";
    }

    @GetMapping("/licenses/{softwareId}")
    public String viewSoftwareLicenses(@PathVariable("softwareId") int id, Model model) {

        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        Optional<Software> optionalSoftware = softwareService.findById(id);
        if (optionalSoftware.isEmpty()) {
            return "redirect:/software";
        }

        Software software = optionalSoftware.get();

        model.addAttribute("software", software);
        model.addAttribute("licenses", software.getLicenses());

        return "software/licenses";
    }
}
