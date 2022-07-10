package dev.drawethree.LicenseSystem.software.controller;

import dev.drawethree.LicenseSystem.security.service.SecurityService;
import dev.drawethree.LicenseSystem.software.model.Software;
import dev.drawethree.LicenseSystem.software.service.SoftwareService;
import dev.drawethree.LicenseSystem.software.validation.SoftwareValidator;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final UserService userService;

    public SoftwareController(SoftwareService softwareService, SecurityService securityService, SoftwareValidator softwareValidator, UserService userService) {
        this.softwareService = softwareService;
        this.securityService = securityService;
        this.softwareValidator = softwareValidator;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CREATOR', 'ADMIN')")
    public String showIndex(Model model) {

        User user = securityService.getCurrentUser();

        List<Software> softwareList = softwareService.findAllByCreator(user);

        model.addAttribute("softwares", softwareList);

        return "software/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATOR', 'ADMIN')")
    public String showCreateNewSoftwareForm(Model model) {

        model.addAttribute("software", new Software());
        return "software/create";
    }

    @GetMapping("/list")
    public String showPublicSoftware(@RequestParam("page") Optional<Integer> page, Model model) {

        int currentPage = page.orElse(1);

        if (currentPage <= 0) {
            currentPage = 1;
        }

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

    @GetMapping("/update")
    @PreAuthorize("hasAnyAuthority('CREATOR', 'ADMIN')")
    public String showUpdateSoftwareForm(@RequestParam("id") int id, Model model) {

        User user = securityService.getCurrentUser();

        Optional<Software> softwareOptional = softwareService.findById(id);

        if (softwareOptional.isEmpty()) {
            return "software/index";
        }

        Software software = softwareOptional.get();

        if (!software.getCreator().equals(user) && !user.isAdmin()) {
            return "redirect:/error/error-401";
        }

        model.addAttribute("software", software);

        return "software/update";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('CREATOR', 'ADMIN')")
    public String updateSoftware(@ModelAttribute("software") Software tempSoftware, BindingResult bindingResult, RedirectAttributes redirectAttributes) {


        User user = securityService.getCurrentUser();

        softwareValidator.validate(tempSoftware, bindingResult);

        if (bindingResult.hasErrors()) {
            return "software/update";
        }

        Optional<Software> optionalSoftware = softwareService.findById(tempSoftware.getId());

        if (optionalSoftware.isEmpty()) {
            return "software/index";
        }

        Software software = optionalSoftware.get();

        if (!software.getCreator().equals(user) && !user.isAdmin()) {
            return "redirect:/error/error-401";
        }

        software.setName(tempSoftware.getName());
        software.setDescription(tempSoftware.getDescription());
        software.setVisible(tempSoftware.isVisible());
        software.setPrice(tempSoftware.getPrice());

        softwareService.updateSoftware(software);

        redirectAttributes.addFlashAttribute("success", "Successfully updated software "  + software.getName() + ".");

        return "redirect:/software";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAnyAuthority('CREATOR', 'ADMIN')")
    public String deleteSoftware(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {

        User currentUser = securityService.getCurrentUser();

        Optional<Software> softwareOptional = softwareService.findById(id);

        if (softwareOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Software does not exist.");
            return "redirect:/software";
        }

        Software software = softwareOptional.get();

        if (!software.getCreator().equals(currentUser) && !currentUser.isAdmin()) {
            return "redirect:/error/error-401";
        }

        softwareService.deleteSoftwareById(id);

        redirectAttributes.addFlashAttribute("success", "Successfully deleted software " + software.getName() + ".");

        return "redirect:/software";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('CREATOR', 'ADMIN')")
    public String createSoftware(@ModelAttribute("software") Software software, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        softwareValidator.validate(software, bindingResult);

        if (bindingResult.hasErrors()) {
            return "software/create";
        }

        User user = securityService.getCurrentUser();

        if (!userService.canCreateSoftware(user)) {
            redirectAttributes.addFlashAttribute("error", "You have reached maximum amount of software for your account!");
            return "redirect:/software";
        }

        software.setCreatedAt(LocalDateTime.now());
        software.setCreator(user);

        softwareService.save(software);

        redirectAttributes.addFlashAttribute("success", "Successfully created software " + software.getName() + ".");

        return "redirect:/software";
    }

    @GetMapping("/licenses")
    @PreAuthorize("hasAnyAuthority('CREATOR', 'ADMIN')")
    public String viewSoftwareLicenses(@RequestParam("softwareId") int id, Model model) {

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
