package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Utils.ICache;

public class RootController {

	private HashMap<String, List<ICache>> cacheHash = new HashMap<>();
	
	private HashMap<Integer, CEntitiy> hashOne;
	private HashMap<Integer, Practice> hashTwo;
	private HashMap<Integer, Payer> hashThree;
	private HashMap<Integer, Provider> hashFour;

	// Generic Code
	public void addList(List<ICache> list, String key){
		
		cacheHash.put(key, list);
	}	
	/*public ICache getObject(String listKey, Integer objectId)
	{
		cacheHash.get(listKey).get(objectId);
	}*/
	
	//
	
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
		hashTwo.clear();
	}

	public Practice getPractiseById(Integer id) {

		return hashTwo.get(id);
	}
	// --------Payer Hash -------------------------------

	public void initHashThree(List<Payer> list) {

		hashThree = new HashMap<>();

		updateHashThree(list);
	}

	public void updateHashThree(List<Payer> list) {

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
}
