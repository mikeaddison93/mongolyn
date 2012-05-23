/* 
   Copyright (C) 2011, Christian Trutz <christian.trutz@belaso.de>

   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
 */
package de.belaso.mongolyn.ui;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author Christian Trutz
 * 
 */
public class Activator implements BundleActivator {

	public static Activator INSTANCE = null;

	private static BundleContext bundleContext = null;

	@Override
	public void start(BundleContext context) throws Exception {
		INSTANCE = this;
		bundleContext = context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		bundleContext = null;
	}

	protected IStatus handleException(Exception exception) {
		IStatus status = new Status(IStatus.ERROR, bundleContext.getBundle()
				.getSymbolicName(), exception.getClass().getName() + ": "
				+ exception.getMessage(), exception);
		ILog logger = Platform.getLog(bundleContext.getBundle());
		logger.log(status);
		return status;
	}
}
