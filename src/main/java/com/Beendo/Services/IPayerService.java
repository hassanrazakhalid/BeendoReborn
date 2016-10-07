package com.Beendo.Services;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.IPayer;
import com.Beendo.Entities.Payer;

public interface IPayerService extends GenericService<Payer, Integer> {

	public boolean isNameExist(String name);
}
