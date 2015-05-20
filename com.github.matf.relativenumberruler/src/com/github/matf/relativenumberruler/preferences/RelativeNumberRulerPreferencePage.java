package com.github.matf.relativenumberruler.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import com.github.matf.relativenumberruler.Activator;

public class RelativeNumberRulerPreferencePage extends
		FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public RelativeNumberRulerPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	public void createFieldEditors() {
		addField(new BooleanFieldEditor(
				PreferenceConstants.SHOW_CURRENT_LINE_NUMBER_ABSOLUTE,
				"&Show absolute value for current line number",
				getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}

}