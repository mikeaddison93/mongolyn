/* 
   Copyright (C) 2011, Christian Trutz <christian.trutz@belaso.de>

   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
 */
package de.belaso.mongolyn.ui;

import java.net.UnknownHostException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;

/**
 * 
 * @author Christian Trutz
 * 
 */
public class MongolynUtils {

	private static DB db = null;

	public static DBCollection getDBCollection(final TaskRepository repository)
			throws CoreException {
		return openDB(repository).getCollection("mongolyn");
	}

	public static DB openDB(final TaskRepository repository)
			throws CoreException {
		if (db == null)
			db = openNewDB(repository);
		return db;
	}

	public static DB openNewDB(final TaskRepository repository)
			throws CoreException {
		try {
			MongoURI mongoURI = new MongoURI(repository.getRepositoryUrl());
			DB db = new Mongo(mongoURI).getDB(mongoURI.getDatabase());
			AuthenticationCredentials credentials = repository
					.getCredentials(AuthenticationType.REPOSITORY);
			if (credentials != null) {
				db.authenticate(credentials.getUserName(), credentials
						.getPassword().toCharArray());
			}
			return db;
		} catch (UnknownHostException unknownHostException) {
			throw new CoreException(
					Activator.INSTANCE.handleException(unknownHostException));
		}
	}

}
