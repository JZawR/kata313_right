package ru.kata.spring.boot_security.demo.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Role> getRoleList() {
		return entityManager.createQuery("from Role", Role.class).getResultList();
	}

	@Override
	public List<String> getRoleNamesToList() {
		return entityManager.createQuery("select roleName from Role").getResultList();
	}

	@Override
	public Role getRoleByRoleName(String roleName) {
		return entityManager
				.createQuery("FROM Role WHERE roleName =:roleName", Role.class)
				.setParameter("roleName", roleName)
				.getSingleResult();
	}

	@Override
	public void save(Role role) {
		entityManager.persist(role);
	}

	@Override
	public List<Role> getRolesInIds(Long[] id) {
		return  entityManager
				.unwrap(Session.class)
				.createQuery("FROM Role WHERE id IN :id", Role.class)
				.setParameterList("id", id)
				.getResultList();
	}
}
