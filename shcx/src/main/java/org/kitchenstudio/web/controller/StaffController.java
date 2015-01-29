package org.kitchenstudio.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.kitchenstudio.model.Staff;
import org.kitchenstudio.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/staff")
public class StaffController {
	private final static Logger log = LoggerFactory
			.getLogger(StaffController.class);

	@Autowired
	private StaffService staffService;

	@RequestMapping({ "", "/" })
	String home(Model model) {
		List<Staff> staffs = staffService.findAll();
		model.addAttribute("staffs", staffs);
		return "staff/list";
	}

	@RequestMapping("/add")
	String add(Staff staff) {
		return "staff/new";
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
	String create(@Valid Staff staff, BindingResult result) {

		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			System.out.println(staff.getAddress());
			return "staff/new";
		}
		System.out.println(staff.getAddress());
		staffService.add(staff);
		return "redirect:/staff";
	}
	
	@RequestMapping("/delete/{id}")
	String delete(@PathVariable("id") Staff staff) {
		staffService.delete(staff);
		return "redirect:/staff";
	}

	@RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
	String info(@PathVariable("id") Staff staff, Model model) {
		model.addAttribute(staff);
		return "staff/detail";
	}

	@RequestMapping(value = "/info", method = RequestMethod.POST)
	String info(Staff staff, BindingResult result) {
		if (result.hasErrors()) {
			return "staff/modify";
		}
		staffService.save(staff);
		return "/staff/modifysuccess";
	}

	@RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
	String modify(@PathVariable("id") Staff staff, Model model) {
		model.addAttribute(staff);
		return "staff/modify";
	}
}
