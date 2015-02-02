package org.kitchenstudio.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.kitchenstudio.entity.Contact;
import org.kitchenstudio.entity.ContactItem;
import org.kitchenstudio.repository.ContactItemRepository;
import org.kitchenstudio.repository.ContactRepository;
import org.kitchenstudio.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

	private final ContactRepository contactRepository;
	private final ContactItemRepository contactItemRepository;

	@Autowired
	public ContactServiceImpl(ContactRepository contactRepository,
			ContactItemRepository contactItemRepository) {
		this.contactRepository = contactRepository;
		this.contactItemRepository = contactItemRepository;
	}

	@Override
	public List<Contact> findAll() {
		return contactRepository.findAll();
	}

	@Override
	public void save(Contact contact, List<ContactItem> contactItems) {
		List<ContactItem> savedItems = new LinkedList<>();
		for (ContactItem contactItem : contactItems) {
			savedItems.add(contactItemRepository.save(contactItem));
		}

		contact.setContactItems(savedItems);
		contactRepository.save(contact);
	}

}