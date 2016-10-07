package com.Beendo.Dao;

import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Plan;

@Repository
public class PlanDao extends GenericDao<Plan, Integer> implements IPlan {

}
