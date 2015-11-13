package com.Beendo.Dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import com.Beendo.Entities.User;

public interface IUserDao extends ICRUD <User, Integer> {

	public User isUserValid(String email, String password);
}
