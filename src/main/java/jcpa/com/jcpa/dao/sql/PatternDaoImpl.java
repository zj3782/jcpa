package com.jcpa.dao.sql;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.jcpa.beans.Pattern;
import com.jcpa.dao.sql.interfaces.PatternDao;
import com.jcpa.util.HibernateUtil;

public class PatternDaoImpl implements PatternDao{
	
	public PatternDaoImpl(){
		
	}
	
	public boolean add(Pattern p) {
		Session s=HibernateUtil.getSession();
		s.beginTransaction();
		s.save(p);
		s.getTransaction().commit();
		return true;
	}

	public int delete(int id) {
		int delCount=0;
		Session s=HibernateUtil.getSession();
		s.beginTransaction();
		Query query=s.createQuery("delete from Pattern where id="+id);
		delCount=query.executeUpdate();
		s.getTransaction().commit();
		return delCount;
	}
	
	public int delete(String[] ids){
		String StrIds="";
		int iid=0;
		for(String id:ids){
			try{
				iid=Integer.parseInt(id);
			}catch(Exception e){
				continue;
			}
			StrIds+=iid+",";
		}
		if(StrIds=="")return 0;
		StrIds=StrIds.substring(0,StrIds.length()-1);
		int delCount=0;
		Session s=HibernateUtil.getSession();
		s.beginTransaction();
		Query query=s.createQuery("delete from Pattern where id in ("+StrIds+")");
		delCount=query.executeUpdate();
		s.getTransaction().commit();
		return delCount;
	}

	public boolean update(Pattern p) {
		Session s=HibernateUtil.getSession();
		s.flush();
        s.clear();
		s.beginTransaction();
		s.update(p);
		s.getTransaction().commit();
		return true;
	}

	public long count(String where){
		Session session = HibernateUtil.getSession();

		String HQL_rows = "select count(*) from Pattern ";
		if(where!=null && !where.equals(""))HQL_rows+="where "+where;//条件
		Long rows=(Long)session.createQuery(HQL_rows).list().get(0);
		return rows.longValue();
	}
	
	public List<Pattern> list(int page,int onePageCount,String where){
		if(page<=0)page=1;
		Session session = HibernateUtil.getSession();
		
		String QuerySql="from Pattern ";
		if(where!=null && !where.equals(""))QuerySql+="where "+where;//条件
		Query query=session.createQuery(QuerySql);
		query.setFirstResult( (page-1)*onePageCount );
		query.setMaxResults(onePageCount);

		@SuppressWarnings("unchecked")
		List<Pattern> result = query.list();

		return result;
	}
	
	public Pattern get(int id){
		Session session = HibernateUtil.getSession();
		Pattern p=(Pattern) session.get(Pattern.class,id);
		return p;
	}
}
