package com.Beendo.Services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.IEntity;
import com.Beendo.Dao.IUserDao;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;

public interface IPractiseService extends GenericService<Practice, Integer> {

	public List<Practice> fetchAllByUser();
	public List<Practice> fetchAllByRole();
	public String checkDuplicateUsername(String name);
	public void updatePractiseList(Set<Practice>list);
	public List<Practice> getPracticeByUser(Integer userId);
}
