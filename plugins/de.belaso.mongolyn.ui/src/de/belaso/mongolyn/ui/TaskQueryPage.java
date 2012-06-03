/* 
   Copyright (C) 2011, Christian Trutz <christian.trutz@belaso.de>

   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
 */
package de.belaso.mongolyn.ui;

import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Christian Trutz
 * 
 */
public class TaskQueryPage extends AbstractRepositoryQueryPage {

	private final IRepositoryQuery query;

	private Text summary = null;

	public TaskQueryPage(TaskRepository taskRepository, IRepositoryQuery query) {
		super("Mongolyn query page", taskRepository, query);
		this.query = query;
	}

	@Override
	public void createControl(Composite parent) {
		Composite root = new Composite(parent, SWT.NONE);
		root.setLayout(new GridLayout(2, false));

		new Label(root, SWT.NONE).setText("Query name:");
		summary = new Text(root, SWT.BORDER);
		if (query != null)
			summary.setText(query.getSummary());
		summary.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setPageComplete(isPageComplete());
			}
		});

		setControl(root);
	}

	@Override
	public String getQueryTitle() {
		if (summary != null)
			return summary.getText();
		return null;
	}

	@Override
	public void applyTo(IRepositoryQuery query) {
		if (summary != null)
			query.setSummary(summary.getText());
	}

}
