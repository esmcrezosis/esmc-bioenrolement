package com.esmc.client.settings;

public final class InfoField {

	// ==============================================
	// Private fields
	// ==============================================

	private String key;
        private Object value = "";
	private boolean showAsThumbnail;
	private boolean enrollToServer;
	private boolean isEditable = true;

	// ==============================================
	// Public constructors
	// ==============================================

	public InfoField() {
	}

	public InfoField(String value) {
		if (value == null) {
			throw new IllegalArgumentException("value");
		}

		String[] items = value.trim().split(",");
		for (String item : items) {
			String str = item.trim();
			String lower = str.toLowerCase();
			int first = str.indexOf("'");
			int last = str.lastIndexOf("'");
			String v = str.substring(first + 1, last);
			if (lower.startsWith("key")) {
				key = v;
			} else if (lower.startsWith("isthumbnail")) {
				showAsThumbnail = Boolean.valueOf(v);
				if (showAsThumbnail) {
					this.value = null;
				}
			} else if (lower.startsWith("enrolltoserver")) {
				enrollToServer = Boolean.valueOf(v);
			}
		}
                
                //personnalisation du code
                // insertion de code membre
               //lire un fichier et inserer le code membre 
                                
                
	}

	// ==============================================
	// Public methods
	// ==============================================

	@Override
	public String toString() {
		StringBuilder resultBuilder = new StringBuilder();
		resultBuilder.append(String.format("Key = '%s'", key));
		if (showAsThumbnail) {
			resultBuilder.append(", IsThumbnail = 'True'");
		}
		if (enrollToServer) {
			resultBuilder.append(", EnrollToServer = 'True'");
		}
		return resultBuilder.toString();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isShowAsThumbnail() {
		return showAsThumbnail;
	}

	public void setShowAsThumbnail(boolean showAsThumbnail) {
		this.showAsThumbnail = showAsThumbnail;
	}

	public boolean isEnrollToServer() {
		return enrollToServer;
	}

	public void setEnrollToServer(boolean enrollToServer) {
		this.enrollToServer = enrollToServer;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

}
