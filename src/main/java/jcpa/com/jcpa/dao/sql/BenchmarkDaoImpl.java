package com.jcpa.dao.sql;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.jcpa.beans.Benchmark;
import com.jcpa.dao.sql.interfaces.BenchmarkDao;
import com.jcpa.util.HibernateUtil;

public class BenchmarkDaoImpl implements BenchmarkDao{
	
	public BenchmarkDaoImpl(){
		
	}
	
	public long count(String where){
		Session session = HibernateUtil.getSession();

		String HQL_rows = "select count(*) from Benchmark ";
		if(where!=null && !where.equals(""))HQL_rows+="where "+where;//条件
		Long rows=(Long)session.createQuery(HQL_rows).list().get(0);
		return rows.longValue();
	}
	
	
	public List<Benchmark> list(int page,int onePageCount,String where){
		if(page<=0)page=1;
		Session session = HibernateUtil.getSession();
		
		String QuerySql="from Benchmark";
		if(where!=null && !where.equals(""))QuerySql+="where "+where;//条件
		Query query=session.createQuery(QuerySql);
		query.setFirstResult( (page-1)*onePageCount );
		query.setMaxResults(onePageCount);
		
		@SuppressWarnings("unchecked")
		List<Benchmark> result = query.list();

		return result;
	}
	
	public boolean update(Benchmark b){
		Session s=HibernateUtil.getSession();
		s.beginTransaction();
		s.update(b);
		s.getTransaction().commit();
		return true;
	}
	public int update(String name,String result){
		Session session = HibernateUtil.getSession();

		String sql = "update Benchmark set result='"+result+"' where name='"+name+"'";
		return session.createQuery(sql).executeUpdate();
	}
}
