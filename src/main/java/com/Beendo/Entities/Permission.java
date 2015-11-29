package com.Beendo.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.Beendo.Utils.Constants;
import com.Beendo.Utils.Role;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PERMISSIONS")
public class Permission   {
//	implements GrantedAuthority
	@Id
	@GeneratedValue
	private Integer id;
	private boolean canProviderAdd;
	private boolean canProviderEdit;
	private boolean canPayerAdd;
	private boolean canPayerEdit;
	private boolean canPayerTransactionAdd;
	private boolean canPayerTransactionEdit;
	
	@OneToOne(mappedBy = "permission")
	private User user;

	public static Permission copy(Permission sender){
		
		Permission rol = new Permission();
		rol.setCanPayerAdd(sender.isCanPayerAdd());
		rol.setCanPayerEdit(sender.isCanPayerEdit());
		rol.setCanProviderAdd(sender.isCanProviderAdd());
		rol.setCanProviderEdit(sender.isCanProviderEdit());
		rol.setCanPayerTransactionAdd(sender.isCanPayerTransactionAdd());
		rol.setCanPayerTransactionEdit(sender.isCanPayerTransactionEdit());
//		rol.setName(sender.getName());
		
		return rol;
		
	}

/*	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
//		return "ROLE_ADMIN";
		String str = null;
		if(isSuperAdmin())
			str = "ROLE_"+Role.ROLE_SUPER_ADMIN;
		else
			str = "ROLE_"+this.getName().toUpperCase();
		return str;
	}*/
}
