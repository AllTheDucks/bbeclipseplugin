package com.blackboard.eclipse.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IManagedForm;

import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.*;
import org.eclipse.ui.forms.editor.*;
import org.eclipse.ui.forms.events.*;
import org.eclipse.ui.forms.widgets.*;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.blackboard.eclipse.BbPlugin;

public class ContentHandlersFormPage extends FormPage {

	/**
	 * @param id
	 * @param title
	 */
	public ContentHandlersFormPage(FormEditor editor) {
		super(editor, "second", "Content Handlers"); //$NON-NLS-1$ //$NON-NLS-2$
		//		super(editor, "second", Messages.getString("SecondPage.label")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected void createFormContent(IManagedForm managedForm) {

		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Application Definitions"); //$NON-NLS-1$
		toolkit.decorateFormHeading(form.getForm());
		ImageDescriptor icon = AbstractUIPlugin.imageDescriptorFromPlugin(
				BbPlugin.PLUGIN_ID, "icons/b2-icon-16.png");
		form.setImage(icon.createImage());
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		form.getBody().setLayout(layout);
	}
}
