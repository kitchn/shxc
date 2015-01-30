package org.kitchenstudio.web.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.kitchenstudio.model.Contact;
import org.kitchenstudio.model.ContactItem;
import org.kitchenstudio.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/contact")
public class ContactController {

	@Autowired
	private ContactService contactService;

	@RequestMapping({ "", "/" })
	String home(Model model) {

		Contact contact = new Contact();
		contact.setLessor("seal");
		contact.setLessee("one");

		ContactItem item = new ContactItem();
		item.setKind("钢管");
		item.setPrice(new BigDecimal(10));

		contactService.save(contact, Collections.singletonList(item));

		List<Contact> contacts = contactService.findAll();
		model.addAttribute("contacts", contacts);
		return "contact/home";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	String detail(@PathVariable("id") Contact contact, Model model) {
		model.addAttribute(contact);
		return "contact/detail";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	String create(Model model) {
		model.addAttribute(new Contact());
		return "contact/new";
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	String create(Contact contact, BindingResult result) {

		return "redirect:/contact";
	}
}
