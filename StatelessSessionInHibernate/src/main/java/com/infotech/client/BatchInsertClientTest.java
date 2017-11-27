package com.infotech.client;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.infotech.entities.Person;
import com.infotech.util.HibernateUtil;

public class BatchInsertClientTest {

	public static void main(String[] args) {
		Transaction tx =  null;
		int batchSize = 5;
		try(Session session = HibernateUtil.getSessionFactory().openSession() ) {
			tx = session.beginTransaction();
		//	for ( int i = 1; i <= 100_000; i++ ) {
			for ( int i = 1; i <= 1000; i++ ) {
				Person  person = new Person();
				person.setFirstName("Gavin_"+i);
				person.setLastName("King_"+i);
				session.persist(person);
				if ( i > 0 && i % batchSize == 0 ) {
					System.out.println("flush and clear the session");
		            //flush a batch of inserts and release memory
		            session.flush();
		            session.clear();
		        }
		    }
			tx.commit();
		} catch (Exception e) {
			if(tx != null && tx.isActive())
				tx.rollback();
			throw e;
		}
	}
}