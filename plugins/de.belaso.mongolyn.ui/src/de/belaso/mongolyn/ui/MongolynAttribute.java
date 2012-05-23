/* 
   Copyright (C) 2011, Christian Trutz <christian.trutz@belaso.de>

   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
 */
package de.belaso.mongolyn.ui;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * 
 * @author Christian Trutz
 * 
 */
public enum MongolynAttribute {

	SUMMARY(TaskAttribute.SUMMARY, "Summary:",
			TaskAttribute.TYPE_SHORT_RICH_TEXT),

	DESCRIPTION(TaskAttribute.DESCRIPTION, "Description:",
			TaskAttribute.TYPE_LONG_RICH_TEXT);

	private final String id, kind, label, type;

	private final boolean readOnly, disabled;

	private MongolynAttribute(String kind, String id, String label,
			String type, boolean readOnly, boolean disabled) {
		this.id = id;
		this.kind = kind;
		this.label = label;
		this.readOnly = readOnly;
		this.type = type;
		this.disabled = disabled;
	}

	private MongolynAttribute(String id, String label, String type) {
		this(TaskAttribute.KIND_DEFAULT, id, label, type, false, false);
	}

	private MongolynAttribute(String id, String label, String type,
			boolean readOnly) {
		this(TaskAttribute.KIND_DEFAULT, id, label, type, readOnly, false);
	}

	public String getKind() {
		return kind;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getType() {
		return type;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public boolean isDisabled() {
		return disabled;
	}

}
