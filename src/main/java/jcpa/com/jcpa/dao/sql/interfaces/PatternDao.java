package com.jcpa.dao.sql.interfaces;

import java.util.List;

import com.jcpa.beans.Pattern;


public interface PatternDao {
	public boolean add(Pattern p);
	public boolean update(Pattern p);
	public int delete(int id);
	public int delete(String[] ids);
	public long count(String where);
	public List<Pattern> list(int page,int onePageCount,String where);
	public Pattern get(int id);
}
