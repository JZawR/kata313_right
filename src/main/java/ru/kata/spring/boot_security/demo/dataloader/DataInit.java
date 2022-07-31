package ru.kata.spring.boot_security.demo.dataloader;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.Set;

@Component
public class DataInit implements ApplicationRunner {

	private final UserDao userDao;
	private final RoleDao roleDao;
	private final PasswordEncoder passwordEncoder;

	public DataInit(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.roleDao = roleDao;
		this.passwordEncoder = passwordEncoder;
	}
@Transactional
	public void run(ApplicationArguments args) {
		var adminRole = new Role("ADMIN");
		roleDao.save(adminRole);
		var userRole = new Role("USER");
		roleDao.save(userRole);

		var user = new User("user", encode("user"), "User", "Userov", "11");
		user.setRoles(Set.of(userRole));
		userDao.addUser(user);

		var user2 = new User("user2", encode("user2"), "User2", "Userov2", "11");
		user2.setRoles(Set.of(userRole));
		userDao.addUser(user2);

		var admin = new User("admin", encode("admin"), "admin", "adminov", "11");
		admin.setRoles(Set.of(adminRole));
		userDao.addUser(admin);

		var userAdmin = new User("useradmin", encode("useradmin"), "useradmin", "useradminov", "11");
		userAdmin.setRoles(Set.of(userRole, adminRole));
		userDao.addUser(userAdmin);
	}

	private String encode(String pass) {
		return passwordEncoder.encode(pass);
	}
}
