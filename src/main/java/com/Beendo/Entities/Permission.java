package com.Beendo.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "permissions")
public class Permission   {
//	implements GrantedAuthority
	@Id
	@GeneratedValue
	private Integer id;
	private boolean canProviderAdd = false;
	private boolean canProviderEdit = false;
	private boolean canPayerTransactionAdd = false;
	private boolean canPayerTransactionEdit = false;
	
	@OneToOne(mappedBy = "permission")
	private User user;

	public static Permission copy(Permission sender){
		
		Permission rol = new Permission();
		rol.setCanProviderAdd(sender.isCanProviderAdd());
		rol.setCanProviderEdit(sender.isCanProviderEdit());
		rol.setCanPayerTransactionAdd(sender.isCanPayerTransactionAdd());
		rol.setCanPayerTransactionEdit(sender.isCanPayerTransactionEdit());
//		rol.setName(sender.getName());
		
		return rol;
		
	}

	public void selectAllPermissions(){
		
		this.setCanProviderAdd(true);
		this.setCanProviderEdit(true);
		this.setCanPayerTransactionAdd(true);
		this.setCanPayerTransactionAdd(true);
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
