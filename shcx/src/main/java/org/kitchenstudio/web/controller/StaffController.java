package org.kitchenstudio.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kitchenstudio.entity.GenderType;
import org.kitchenstudio.entity.Staff;
import org.kitchenstudio.entity.StaffType;
import org.kitchenstudio.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/staff")
public class StaffController {
	@Autowired
	private StaffService staffService;
	
	@ModelAttribute("genders")
	public GenderType[] populateGenders() {
		return GenderType.values();
	}
	
	@ModelAttribute("staffTypes")
	public StaffType[] populateStaffTypes() {
		return StaffType.values();
	}

	@RequestMapping({ "", "/" })
	String home(Model model) {
		List<Staff> staffs = staffService.findAll();
		model.addAttribute("staffs", staffs);
		return "staff/list";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	String create(Model model) {
		if (!model.containsAttribute("staff")) {
			Staff staff = new Staff();
			model.addAttribute(staff);
		}
		return "staff/new";
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	String create(@RequestParam("file") MultipartFile file, @Valid Staff staff,
			BindingResult result, HttpServletRequest request) {
		if (result.hasErrors()) {
			return "staff/new";
		}

		String pathFaceImage = getFaceimagePath(request, file);
		if (pathFaceImage != null) {
			staff.setPathFaceimage(pathFaceImage);
		}

		staffService.add(staff);
		return "redirect:/staff";
	}

	@RequestMapping("/{id}/delete")
	String delete(@PathVariable("id") Staff staff) {
		staffService.delete(staff);
		return "redirect:/staff";
	}

	@RequestMapping(value = "/{id}/info", method = RequestMethod.GET)
	String info(@PathVariable("id") Staff staff, Model model) {
		model.addAttribute(staff);
		return "staff/detail";
	}

	@RequestMapping(value = "/{id}/modify", method = RequestMethod.GET)
	String modify(@PathVariable("id") Staff staff, Model model) {
		model.addAttribute(staff);
		return "staff/modify";
	}

	@RequestMapping(value = "/{id}/modify", method = RequestMethod.POST)
	String info(@RequestParam("file") MultipartFile file, @Valid Staff staff,
			BindingResult result, HttpServletRequest request, Model model) {
		if (result.hasErrors()) {
			return "staff/modify";
		}

		String pathFaceImage = getFaceimagePath(request, file);
		System.out.println(pathFaceImage);
		if (pathFaceImage != null) {
			staff.setPathFaceimage(pathFaceImage);
			System.out.println(pathFaceImage);
		}

		staffService.save(staff);
		return "redirect:/staff/{id}/info?success";
	}

	private String getFaceimagePath(HttpServletRequest request,
			MultipartFile file) {
		// 只有当照片大小大于0才处理
		if (file.getSize() <= 0)
			return null;

		String conPath = null;
		String sysPath = request.getServletContext().getRealPath("/");
		try {

			File fileUpload = new File(sysPath + "/upload");
			if (!fileUpload.isDirectory()) {
				fileUpload.mkdir();
			} else {
				fileUpload.delete();
				fileUpload.mkdir();
			}
			String[] name = file.getOriginalFilename().split("\\.");
			String suffix = name[name.length - 1];
			File tempFile = File
					.createTempFile("img", "." + suffix, fileUpload);
			file.transferTo(tempFile);
			conPath = request.getContextPath() + "/upload/"
					+ tempFile.getName();
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return conPath;
	}
}
