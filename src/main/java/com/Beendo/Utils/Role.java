package com.Beendo.Utils;

import javax.persistence.EnumType;

public enum Role {

	ENTITY_USER("ENTITY_USER"),
	ENTITY_ADMIN("ENTITY_ADMIN"),
	ROOT_USER("ROOT_USER"),
	ROOT_ADMIN("ROOT_ADMIN");
	
//	ROOT_ADMIN {
//		public String toString() {
//			return "ROOT_ADMIN";
//		}
//	},
//
//	ROOT_USER {
//		public String toString() {
//			return "ROOT_USER";
//		}
//	},
//	ENTITY_ADMIN {
//		public String toString() {
//			return "ENTITY_ADMIN";
//		}
//	},
//
//	ENTITY_USER {
//		public String toString() {
//			return "ENTITY_USER";
//		}
//	};
//	
	
	 Role(String str) {
		this.roleStr = str;
	}
	 
	 static public Role getRole(String str) {
		 
		 return (Role) Enum.valueOf(Role.class, str);
	 }
	 
	protected String roleStr;
	

}
