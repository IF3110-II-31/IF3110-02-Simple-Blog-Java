package com.github.if3110_31.simple_blog.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.model.SelectItem;

import com.github.if3110_31.simple_blog.db.DAOFactory;
import com.github.if3110_31.simple_blog.db.UserSessionDAO;
import com.github.if3110_31.simple_blog.model.User;
import com.github.if3110_31.simple_blog.model.User.Role;

@ManagedBean(name = "roleBean")
@ApplicationScoped
public class RoleBean {
	private SelectItem[] roleList;
	
	@PostConstruct
	private void init() {
		clearSession();
		
		Role[] roles = Role.values();
		roleList = new SelectItem[roles.length];
		
		for(int i = 0; i < roleList.length; i++) {
			roleList[i] = new SelectItem(roles[i], roles[i].getDescription());
		}
	}
	
	private static void clearSession() {
		UserSessionDAO dao = DAOFactory.getInstance("simple-blog").getUserSessionDAO();
		
		dao.clearUserSession();
	}

	public SelectItem[] getRoleList() {
		return roleList;
	}
}
