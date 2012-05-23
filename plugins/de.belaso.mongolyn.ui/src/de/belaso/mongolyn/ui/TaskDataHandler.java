/* 
   Copyright (C) 2011, Christian Trutz <christian.trutz@belaso.de>

   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
 */
package de.belaso.mongolyn.ui;

import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.RepositoryResponse.ResponseKind;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * 
 * @author Christian Trutz
 * 
 */
public class TaskDataHandler extends AbstractTaskDataHandler {

	@Override
	public boolean initializeTaskData(TaskRepository repository, TaskData data,
			ITaskMapping initializationData, IProgressMonitor monitor)
			throws CoreException {
		TaskAttribute rootAttribute = data.getRoot();
		for (MongolynAttribute mongolynAttribute : MongolynAttribute.values()) {
			TaskAttribute newAttribute = rootAttribute
					.createAttribute(mongolynAttribute.getId());
			newAttribute.getMetaData().defaults()
					.setKind(mongolynAttribute.getKind())
					.setLabel(mongolynAttribute.getLabel())
					.setReadOnly(mongolynAttribute.isReadOnly())
					.setType(mongolynAttribute.getType())
					.setDisabled(mongolynAttribute.isDisabled());
		}
		return true;
	}

	@Override
	public RepositoryResponse postTaskData(TaskRepository repository,
			TaskData taskData, Set<TaskAttribute> oldAttributes,
			IProgressMonitor monitor) throws CoreException {
		DBCollection dbCollection = MongolynUtils.getDBCollection(repository);
		BasicDBObjectBuilder bob = BasicDBObjectBuilder.start();
		for (Map.Entry<String, TaskAttribute> entry : taskData.getRoot()
				.getAttributes().entrySet()) {
			String key = entry.getKey();
			TaskAttribute attribute = entry.getValue();
			String attributeValue = attribute.getValue();
			if (attributeValue != null) {
				bob.add(key.replace('.', '_'), attributeValue);
			}
		}
		DBObject dbObject = bob.get();
		if (taskData.isNew()) {
			dbCollection.insert(dbObject);
			return new RepositoryResponse(ResponseKind.TASK_CREATED, dbObject
					.get("_id").toString());
		} else {
			dbCollection.findAndModify(new BasicDBObject("_id", new ObjectId(
					taskData.getTaskId())), dbObject);
			return new RepositoryResponse(ResponseKind.TASK_UPDATED,
					taskData.getTaskId());
		}
	}

	@Override
	public TaskAttributeMapper getAttributeMapper(TaskRepository repository) {
		return new TaskAttributeMapper(repository);
	}

}
