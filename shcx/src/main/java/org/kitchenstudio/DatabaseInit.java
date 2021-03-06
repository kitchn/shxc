package org.kitchenstudio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.kitchenstudio.entity.Account;
import org.kitchenstudio.entity.Authority;
import org.kitchenstudio.entity.Company;
import org.kitchenstudio.entity.Driver;
import org.kitchenstudio.entity.Product;
import org.kitchenstudio.entity.ProductCategory;
import org.kitchenstudio.entity.ProductSpecification;
import org.kitchenstudio.entity.Site;
import org.kitchenstudio.entity.SiteType;
import org.kitchenstudio.entity.Staff;
import org.kitchenstudio.entity.Store;
import org.kitchenstudio.repository.AccountRepository;
import org.kitchenstudio.repository.AuthorityRepository;
import org.kitchenstudio.repository.CompanyRepository;
import org.kitchenstudio.repository.ContractRepository;
import org.kitchenstudio.repository.DriverRepository;
import org.kitchenstudio.repository.ProductCategoryRepository;
import org.kitchenstudio.repository.ProductRepository;
import org.kitchenstudio.repository.ProductSpecificationRepository;
import org.kitchenstudio.repository.SiteRepository;
import org.kitchenstudio.repository.StaffRepository;
import org.kitchenstudio.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseInit {

	@Bean
	public String initProducts() {
		logger.info("init products");

		ProductCategory category = categoryRepository.save(new ProductCategory(
				"租赁"));

		List<ProductSpecification> specifications = new ArrayList<>();
		for (int i = 1; i < 6; i++) {
			for (int j = 1; j < 10; j++) {
				specifications.add(specificationRepository
						.save(new ProductSpecification(i + "." + j + "米",
								new Double(i + "." + j))));
			}
		}
		specifications.add(specificationRepository
				.save(new ProductSpecification("6.0米", 6.0)));

		productRepository.save(new Product("钢管", category, specifications));

		// 虚拟数据
		for (int i = 0; i < 6; i++) {
			category = categoryRepository.save(new ProductCategory("分类" + i));

			for (int j = 0; j < 6; j++) {

				specifications = new ArrayList<>();
				for (int k = 1; k < 10; k++) {
					specifications.add(specificationRepository
							.save(new ProductSpecification("规格" + k, 1)));
				}
				productRepository.save(new Product("产品" + i + j, category,
						specifications));
			}

		}

		return SUCCESS;
	}

	@Bean
	public String initStaffs() {
		logger.info("init staffs");

		for (int i = 0; i < 10; i++) {
			Staff staff = new Staff();
			staff.setName("王小欣" + i);
			staff.setPhoneNumber("18366110000");
			staff.setIDCard("328333922232323224");
			staff.setBankNumber("1234123412341234123");
			staffRepository.save(staff);
		}

		return SUCCESS;
	}

	@Bean
	public String initDrivers() {
		logger.info("init drivers");

		for (int i = 0; i < 10; i++) {
			Driver driver = new Driver();
			driver.setName("老司机" + i);
			driver.setBankNumber("123456789098765432" + i);
			driver.setIDcard("12345678901234567" + i);
			driver.setNation("" + i);
			driver.setPlateNumber("沪A1234" + i);
			driver.setPhoneNumber("" + i + i + i + i);
			driverRepository.save(driver);
		}

		return SUCCESS;
	}

	@Bean
	public String initCompanies() {
		logger.info("init companies");
		List<Site> sites = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			Company company = new Company();
			company.setAddress("地址" + i);
			company.setCharger("changer" + i);
			company.setName("公司" + i);
			companyRepository.save(company);
			
			Site site = new Site();
			site.setType(SiteType.FIELD);
			site.setName("上海场地" + i);
			site.setCompany(company);
			site.setAddress("上海松江");
			sites.add(siteRepository.save(site));
		}
		
		for (Site site : sites) {
			for (Product product: productRepository.findAll()) {
				Store store = new Store();
				store.setSite(site);
				store.setProduct(product);
				store.setQuantity(new BigDecimal(0));
				storeRepository.save(store);
			}
		}

		return SUCCESS;
	}
	
	@Bean
	public String initAccount() {
		logger.info("init accounts");
		Authority userAuthority= authorityRepository.save(new Authority("USER"));
		Authority adminAuthority= authorityRepository.save(new Authority("ADMIN"));
		
		Account sealAccount = new Account();
		sealAccount.setUsername("seal");
		sealAccount.setPassword("..xiao");
		sealAccount.getAuthorities().add(adminAuthority);
		sealAccount.getAuthorities().add(userAuthority);
		accountRepository.save(sealAccount);


		return SUCCESS;
	}

	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductCategoryRepository categoryRepository;

	@Autowired
	private ProductSpecificationRepository specificationRepository;

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private SiteRepository siteRepository;
	
	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	

	private static final String SUCCESS = "INIT_OK";
	private static final Logger logger = LoggerFactory
			.getLogger(DatabaseInit.class);
}
