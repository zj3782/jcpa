package com.jcpa.dao.sql.interfaces;

import java.util.List;

import com.jcpa.beans.Benchmark;


public interface BenchmarkDao {
	public boolean update(Benchmark b);
	public int update(String name,String result);
	public long count(String where);
	public List<Benchmark> list(int page,int onePageCount,String where);
}
