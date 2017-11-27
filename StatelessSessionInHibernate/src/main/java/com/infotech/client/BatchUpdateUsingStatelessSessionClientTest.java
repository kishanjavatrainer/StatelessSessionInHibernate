package com.infotech.client;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import com.infotech.entities.Person;
import com.infotech.util.HibernateUtil;

public class BatchUpdateUsingStatelessSessionClientTest {

	public static void main(String[] args) {
		StatelessSession statelessSession = null;
		Transaction txn = null;
		ScrollableResults scrollableResults = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			statelessSession = sessionFactory.openStatelessSession();

			txn = statelessSession.getTransaction();
			txn.begin();

			scrollableResults = statelessSession.createQuery("select p from Person p").scroll(ScrollMode.FORWARD_ONLY);

			while (scrollableResults.next()) {
				Person Person = (Person) scrollableResults.get(0);
				processPerson(Person);
				statelessSession.update(Person);
			}

			txn.commit();
		} catch (RuntimeException e) {
			if (txn != null && txn.getStatus() == TransactionStatus.ACTIVE)
				txn.rollback();
			throw e;
		} finally {
			if (scrollableResults != null) {
				scrollableResults.close();
			}
			if (statelessSession != null) {
				statelessSession.close();
			}
		}
	}

	private static void processPerson(Person person) {
		person.setFirstName(person.getFirstName() + "_upadted");
	}
}