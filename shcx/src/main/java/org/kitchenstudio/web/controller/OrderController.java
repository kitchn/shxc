package org.kitchenstudio.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kitchenstudio.entity.Order;
import org.kitchenstudio.entity.OrderItem;
import org.kitchenstudio.entity.OrderType;
import org.kitchenstudio.entity.Staff;
import org.kitchenstudio.entity.Type;
import org.kitchenstudio.service.OrderService;
import org.kitchenstudio.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order")
public class OrderController {

	private static final Logger log = LoggerFactory
			.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private StaffService staffService;

	@ModelAttribute("staffs")
	public List<Staff> populateStaffs() {
		return staffService.findAll();
	}

	@ModelAttribute("types")
	public Type[] populateTypes() {
		return Type.values();
	}

	@RequestMapping({ "", "/" })
	String home(Model model) {
		List<Order> orders = orderService.findAll();
		model.addAttribute("orders", orders);

		return "order/home";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	String create(Order order) {
		return "order/new";
	}

	@RequestMapping(value = "/new", params = { "save" }, method = RequestMethod.POST)
	String create(@Valid Order order, BindingResult result) {
		if (result.hasErrors()) {
			return "order/new";
		}
		orderService.save(order, order.getOrderItems());
		return "redirect:/order";
	}

	@RequestMapping(value = "/new", params = { "addItem" }, method = RequestMethod.POST)
	String addItems(@Valid Order order, BindingResult result) {
		order.getOrderItems().add(new OrderItem());
		return "order/new";
	}

	@RequestMapping(value = "/{id}/info", method = RequestMethod.GET)
	String info(@PathVariable("id") Order order, Model model) {
		model.addAttribute(order);
		return "order/detail";
	}

	@RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
	String modify(@PathVariable("id") Order order, Model model) {
		model.addAttribute(order);
		return "order/modify";
	}

	@RequestMapping(value = "/{id}/info", method = RequestMethod.POST)
	String info(@Valid Order order, BindingResult result) {
		if (result.hasErrors()) {
			return "order/modify";
		}
		orderService.save(order, order.getOrderItems());
		return "redirect:/order/{id}/info?success";
	}

	@RequestMapping(value = "/info", params = { "removeItem" }, method = RequestMethod.POST)
	String modifyRemoveItems(@Valid Order order, BindingResult result,
			HttpServletRequest req) {
		Integer rowId = Integer.valueOf(req.getParameter("removeItem"));
		order.getOrderItems().remove(rowId.intValue());
		return "order/modify";
	}

	@RequestMapping(value = "/info", params = { "modifyAddItem" }, method = RequestMethod.POST)
	String modifyAddItems(@Valid Order order, BindingResult result) {
		order.getOrderItems().add(new OrderItem());
		return "order/modify";
	}

	@RequestMapping("/delete/{id}")
	String delete(@PathVariable("id") Order order) {
		orderService.delete(order);
		return "redirect:/order";
	}
}
