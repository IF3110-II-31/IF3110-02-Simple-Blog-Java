package com.github.if3110_31.simple_blog.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.model.SelectItem;

import com.github.if3110_31.simple_blog.model.User;
import com.github.if3110_31.simple_blog.model.User.Role;

@ManagedBean(name = "roleBean")
@ApplicationScoped
public class RoleBean {
	private SelectItem[] roleList;
	
	@PostConstruct
	private void init() {
		Role[] roles = Role.values();
		roleList = new SelectItem[roles.length];
		
		for(int i = 0; i < roleList.length; i++) {
			roleList[i] = new SelectItem(roles[i], roles[i].getDescription());
		}
	}
	
	public SelectItem[] getRoleList() {
		return roleList;
	}
}
