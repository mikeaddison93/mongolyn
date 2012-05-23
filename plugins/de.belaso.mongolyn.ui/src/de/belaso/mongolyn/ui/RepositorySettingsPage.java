/* 
   Copyright (C) 2011, Christian Trutz <christian.trutz@belaso.de>

   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
 */
package de.belaso.mongolyn.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.internal.tasks.core.IRepositoryConstants;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage;
import org.eclipse.swt.widgets.Composite;

import com.mongodb.DB;
import com.mongodb.MongoURI;

/**
 * 
 * @author Christian Trutz
 * 
 */
@SuppressWarnings("restriction")
public class RepositorySettingsPage extends AbstractRepositorySettingsPage {

	public RepositorySettingsPage(TaskRepository taskRepository) {
		super(Messages.RepositorySettingsPage_Title,
				Messages.RepositorySettingsPage_Description, taskRepository);
		setNeedsProxy(false);
		setNeedsAnonymousLogin(true);
	}

	@Override
	public String getConnectorKind() {
		return RepositoryConnector.KIND;
	}

	@Override
	protected void createAdditionalControls(Composite parent) {
	}

	@Override
	protected boolean isValidUrl(String url) {
		try {
			MongoURI mongoURI = new MongoURI(url);
			if (mongoURI.getDatabase() == null)
				return false;
			else
				return true;
		} catch (RuntimeException runtimeException) {
			return false;
		}
	}

	@Override
	protected Validator getValidator(final TaskRepository repository) {
		return new Validator() {

			@Override
			public void run(IProgressMonitor progressMonitor)
					throws CoreException {
				progressMonitor
						.beginTask(
								Messages.RepositorySettingsPage_ValidateRepositorySettings,
								1);
				DB db = null;
				try {
					db = MongolynUtils.openNewDB(repository);
					db.getCollectionNames();
					progressMonitor.worked(1);
				} finally {
					if (db != null)
						db.getMongo().close();
					progressMonitor.done();
				}
			}
		};
	}

	@Override
	public void applyTo(TaskRepository repository) {
		repository.setProperty(IRepositoryConstants.PROPERTY_CATEGORY,
				IRepositoryConstants.CATEGORY_TASKS);
		super.applyTo(repository);
	}

}
