package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;
import com.Beendo.Entities.Permission;

public class RootController {

	private HashMap<Integer, CEntitiy> hashOne;
	private HashMap<Integer, Practice> hashTwo;
	private HashMap<Integer, Payer> hashThree;
	private HashMap<Integer, Provider> hashFour;
	private HashMap<Integer, ProviderTransaction> hashTrans;
	private HashMap<Integer, Permission> hashPermission;
	
	public void initHashOne(List<CEntitiy> list) {

		hashOne = new HashMap<>();

		updateHashOne(list);
	}

	public void updateHashOne(List<CEntitiy> list) {

		for (CEntitiy object : list) {

			hashOne.put(object.getId(), object);
		}
	}

	public List<CEntitiy> getAllHashOne() {

		Collection<CEntitiy> col = hashOne.values();
		List<CEntitiy> list = new ArrayList(col);
		return list;
	}

	public CEntitiy getEntityById(Integer id) {

		return hashOne.get(id);
	}
	// Practise Has Map

	public void initHashTwo(List<Practice> list) {

		hashTwo = new HashMap<>();

		updateHashTwo(list);
	}

	public void updateHashTwo(List<Practice> list) {

		for (Practice object : list) {

			hashTwo.put(object.getId(), object);
		}
	}

	public List<Practice> getAllHashTwo() {

		Collection<Practice> col = hashTwo.values();
		List<Practice> list = new ArrayList(col);
		return list;
	}

	public void clearHashTwo() {
		
		if(hashTwo != null)
		hashTwo.clear();
	}

	public Practice getPractiseById(Integer id) {

		if(hashTwo.containsKey(id))
			return hashTwo.get(id);
		else return null;
	}
	// --------Payer Hash -------------------------------

	public void initHashThree(Collection<Payer> list) {

		hashThree = new HashMap<>();

		updateHashThree(list);
	}

	public void updateHashThree(Collection<Payer> list) {

		for (Payer object : list) {

			hashThree.put(object.getId(), object);
		}
	}

	public List<Payer> getAllHashThree() {

		Collection<Payer> col = hashThree.values();
		List<Payer> list = new ArrayList(col);
		return list;
	}

	public void clearHashThree() {
		hashThree.clear();
	}

	public Payer getPayerById(Integer id) {

		return hashThree.get(id);
	}

	// Provider ------- Hash

	public void initHashFour(List<Provider> list) {

		hashFour = new HashMap<>();

		updateHashFour(list);
	}

	public void updateHashFour(List<Provider> list) {

		for (Provider object : list) {

			hashFour.put(object.getId(), object);
		}
	}

	public List<Provider> getAllHashFour() {

		Collection<Provider> col = hashFour.values();
		List<Provider> list = new ArrayList(col);
		return list;
	}

	public void clearHashFour() {
		hashFour.clear();
	}

	public Provider getProviderById(Integer id) {

		return hashFour.get(id);
	}

	// ------------------------- TRANSACTION HASH ------------------------

	public void initHash(List<ProviderTransaction> list) {

		hashTrans = new HashMap<>();

		updateHash(list);
	}

	public void updateHash(List<ProviderTransaction> list) {

		for (ProviderTransaction object : list) {

			hashTrans.put(object.getId(), object);
		}
	}

	public List<ProviderTransaction> getAllHash() {

		Collection<ProviderTransaction> col = hashTrans.values();
		List<ProviderTransaction> list = new ArrayList<ProviderTransaction>(col);
		return list;
	}

	public void clearHash() {
		hashTrans.clear();
	}

	public ProviderTransaction getTransactionById(Integer id) {

		return hashTrans.get(id);
	}

	// ------------------- ROLES ------------------------

	public void initPermissionHash(List<Permission> list) {

		hashPermission = new HashMap<>();

		updatePermissionHash(list);
	}

	public void updatePermissionHash(List<Permission> list) {

		for (Permission object : list) {

			hashPermission.put(object.getId(), object);
		}
	}

	public List<Permission> getAllPermission() {

		Collection<Permission> col = hashPermission.values();
		List<Permission> list = new ArrayList<Permission>(col);
		return list;
	}

	public Permission getPermissionById(Integer id) {

		return hashPermission.get(id);
	}

	
	public List<String> reloadRolesList() {

		List<String> listRoles = new ArrayList<>();
		if(!listRoles.isEmpty())
			return listRoles;
		else
		{
			String userRole = SharedData.getSharedInstace().getCurrentUser().getRoleName();
			
			if(userRole.equalsIgnoreCase(Role.ROOT_ADMIN.toString()))
			{
					listRoles.add(Role.ROOT_USER.toString());
					listRoles.add(Role.ENTITY_ADMIN.toString());
					listRoles.add(Role.ENTITY_USER.toString());		
			}
			
			if(userRole.equalsIgnoreCase(Role.ROOT_USER.toString())) //add all except
			{
					listRoles.add(Role.ENTITY_ADMIN.toString());
					listRoles.add(Role.ENTITY_USER.toString());		
			}
			
			if(userRole.equalsIgnoreCase(Role.ENTITY_ADMIN.toString())) //add all except
			{
					listRoles.add(Role.ENTITY_USER.toString());		
			}		

			return listRoles;
		}
	}
}
