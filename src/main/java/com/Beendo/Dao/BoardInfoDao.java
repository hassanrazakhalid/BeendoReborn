
package com.Beendo.Dao;

import org.springframework.stereotype.Repository;

import com.Beendo.Entities.BoardInfo;

@Repository
public class BoardInfoDao extends GenericDao<BoardInfo, Integer> implements IBoardInfo {

}
