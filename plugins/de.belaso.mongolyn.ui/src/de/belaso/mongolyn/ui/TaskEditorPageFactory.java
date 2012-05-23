/* 
   Copyright (C) 2011, Christian Trutz <christian.trutz@gmail.com>

   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
 */
package de.belaso.mongolyn.ui;

import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.ui.ITasksUiConstants;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * 
 * @author Christian Trutz
 * 
 */
public class TaskEditorPageFactory extends AbstractTaskEditorPageFactory {

	@Override
	public boolean canCreatePageFor(TaskEditorInput input) {
		ITask task = input.getTask();
		return RepositoryConnector.KIND.equals(task.getConnectorKind())
				|| TasksUiUtil
						.isOutgoingNewTask(task, RepositoryConnector.KIND);
	}

	@Override
	public Image getPageImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPageText() {
		return "Mongolyn";
	}

	@Override
	public IFormPage createPage(TaskEditor parentEditor) {
		return new TaskEditorPage(parentEditor);
	}

	@Override
	public int getPriority() {
		return PRIORITY_TASK;
	}

	@Override
	public String[] getConflictingIds(TaskEditorInput input) {
		return new String[] { ITasksUiConstants.ID_PAGE_PLANNING };
	}

}
