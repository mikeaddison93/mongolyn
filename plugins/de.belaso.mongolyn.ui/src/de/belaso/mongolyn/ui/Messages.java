package de.belaso.mongolyn.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "de.belaso.mongolyn.ui.messages"; //$NON-NLS-1$
	public static String RepositoryConnector_Label;
	public static String RepositorySettingsPage_Title;
	public static String RepositorySettingsPage_Description;
	public static String RepositorySettingsPage_ValidateRepositorySettings;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
