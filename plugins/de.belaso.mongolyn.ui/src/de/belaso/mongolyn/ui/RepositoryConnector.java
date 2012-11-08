/* 
   Copyright (C) 2011, Christian Trutz <christian.trutz@belaso.de>

   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
 */
package de.belaso.mongolyn.ui;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * 
 * @author Christian Trutz
 * 
 */
public class RepositoryConnector extends AbstractRepositoryConnector {

	//
	public static final String KIND = "mongolyn"; //$NON-NLS-1$

	//
	private final TaskDataHandler taskDataHandler;

	public RepositoryConnector() {
		taskDataHandler = new TaskDataHandler();
	}

	@Override
	public String getConnectorKind() {
		return KIND;
	}

	@Override
	public String getLabel() {
		return Messages.RepositoryConnector_Label;
	}

	@Override
	public AbstractTaskDataHandler getTaskDataHandler() {
		return taskDataHandler;
	}

	@Override
	public boolean canCreateNewTask(TaskRepository repository) {
		return true;
	}

	@Override
	public boolean canCreateTaskFromKey(TaskRepository repository) {
		return true;
	}

	@Override
	public String getRepositoryUrlFromTaskUrl(String taskFullUrl) {
		if (taskFullUrl == null || taskFullUrl.length() == 0)
			return null;
		int lastSlash = taskFullUrl.lastIndexOf('/');
		if (lastSlash != -1 && lastSlash + 1 < taskFullUrl.length())
			return taskFullUrl.substring(0, lastSlash);
		return null;
	}

	@Override
	public String getTaskIdFromTaskUrl(String taskFullUrl) {
		if (taskFullUrl == null || taskFullUrl.length() == 0)
			return null;
		int lastSlash = taskFullUrl.lastIndexOf('/');
		if (lastSlash != -1 && lastSlash + 1 < taskFullUrl.length())
			return taskFullUrl.substring(lastSlash + 1);
		return null;
	}

	@Override
	public String getTaskUrl(String repositoryUrl, String taskId) {
		return repositoryUrl + "/" + taskId;
	}

	@Override
	public boolean hasTaskChanged(TaskRepository taskRepository, ITask task,
			TaskData taskData) {
		// TODO introduce modify date?
		return false;
	}

	@Override
	public TaskData getTaskData(TaskRepository taskRepository, String taskId,
			IProgressMonitor monitor) throws CoreException {
		DBCollection dbCollection = MongolynUtils
				.getDBCollection(taskRepository);
		DBObject dbObject = dbCollection.findOne(new ObjectId(taskId));
		if (dbObject != null) {
			TaskData taskData = new TaskData(getTaskDataHandler()
					.getAttributeMapper(taskRepository), KIND,
					taskRepository.getRepositoryUrl(), taskId);
			taskData.setPartial(false);
			taskData.setVersion("1");
			getTaskDataHandler().initializeTaskData(taskRepository, taskData,
					null, monitor);
			for (String key : dbObject.keySet()) {
				if (!"_id".equals(key))
					taskData.getRoot().getAttribute(key.replace('_', '.'))
							.setValue(dbObject.get(key).toString());
			}
			return taskData;
		} else {
			throw new CoreException(
					Activator.INSTANCE.getErrorStatus("MongoDB document "
							+ taskId + " not found."));
		}
	}

	@Override
	public void updateTaskFromTaskData(TaskRepository taskRepository,
			ITask task, TaskData taskData) {
		if (!taskData.isNew())
			task.setUrl(getTaskUrl(taskRepository.getUrl(),
					taskData.getTaskId()));
		new TaskMapper(taskData).applyTo(task);
	}

	@Override
	public IStatus performQuery(TaskRepository taskRepository,
			IRepositoryQuery query, TaskDataCollector collector,
			ISynchronizationSession session, IProgressMonitor monitor) {
		try {
			DBCollection dbCollection = MongolynUtils
					.getDBCollection(taskRepository);
			DBCursor dbCursor = dbCollection.find();
			while (dbCursor.hasNext()) {
				DBObject dbObject = dbCursor.next();
				TaskData taskData = new TaskData(getTaskDataHandler()
						.getAttributeMapper(taskRepository), KIND,
						taskRepository.getRepositoryUrl(), dbObject.get("_id")
								.toString());
				taskData.setPartial(false);
				taskData.setVersion("1");
				getTaskDataHandler().initializeTaskData(taskRepository,
						taskData, null, monitor);
				for (String key : dbObject.keySet()) {
					if (!"_id".equals(key))
						taskData.getRoot().getAttribute(key.replace('_', '.'))
								.setValue(dbObject.get(key).toString());
				}
				collector.accept(taskData);
			}
		} catch (CoreException coreException) {
			// nothing to do
		}
		return Status.OK_STATUS;
	}

	@Override
	public void updateRepositoryConfiguration(TaskRepository taskRepository,
			IProgressMonitor monitor) throws CoreException {
	}

}
