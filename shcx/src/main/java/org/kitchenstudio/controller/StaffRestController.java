package org.kitchenstudio.controller;

import org.kitchenstudio.entity.Staff;
import org.kitchenstudio.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffRestController {

	@Autowired
	private StaffService staffService;

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	void delete(@PathVariable("id") Staff staff) {
		staffService.delete(staff);
	}
}
