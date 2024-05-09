package com.thebigburd.LibraryApplication.Model.enumeration;

public enum UserRole {
	ROLE_USER("USER"),
	ROLE_ADMIN("ADMIN");

	private final String role;

	UserRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return this.role;
	}
}
