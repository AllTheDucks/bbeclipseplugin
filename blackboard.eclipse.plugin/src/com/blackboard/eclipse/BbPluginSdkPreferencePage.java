package com.blackboard.eclipse;

import java.io.File;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class BbPluginSdkPreferencePage 
	extends PreferencePage
	implements IWorkbenchPreferencePage {

	
	//The newEntryText is the text where new bad words are specified
	private Text sdkRootText;

	
	@Override
	public boolean isValid() {
		File sdkDir = new File(sdkRootText.getText());
		return sdkRootText.getText() != null && sdkDir.exists() && sdkDir.isDirectory();
	}
	
	@Override
	public boolean okToLeave() {
		return true;
	}
	/*
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {

		Composite entryTable = new Composite(parent, SWT.NULL);

		//Create a data that takes up the extra space in the dialog .
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		gd.widthHint = 100;
		entryTable.setLayoutData(gd);

		GridLayout layout = new GridLayout();
		entryTable.setLayout(layout);			
				
		//Add in a dummy label for spacing
		Label desc = new Label(entryTable,SWT.NONE|SWT.WRAP);
		desc.setText("Select the directory containing the blackboard SDKs."+
				"\nAn SDK is a collection of JAR files from the Blackboard systemlib directory."+
				"\nSuggested Location is <Your Home Directory>/bb-sdk/");
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		desc.setSize(100, 100);
		desc.setLayoutData(gd);
//		desc.setData(data);
		
		
		Composite buttonComposite = new Composite(entryTable,SWT.NULL);
		
		GridLayout buttonLayout = new GridLayout();
		buttonLayout.numColumns = 2;
		buttonComposite.setLayout(buttonLayout);

		//Create a data that takes up the extra space in the dialog and spans both columns.
		gd = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING);
		buttonComposite.setLayoutData(gd);		
		
		
		sdkRootText = new Text(buttonComposite, SWT.BORDER|SWT.READ_ONLY);
		//Create a data that takes up the extra space in the dialog .
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		sdkRootText.setLayoutData(gd);

		Button browseButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);

		final Shell parentShell = this.getShell();
		browseButton.setText("Browse..."); //$NON-NLS-1$
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dirDialog = new DirectoryDialog(parentShell);
				sdkRootText.setText(dirDialog.open());
				updateApplyButton();
				getContainer().updateButtons();
			}
		});
		sdkRootText.setText(BbPlugin.getDefault().getSdkRootDirPath());
		noDefaultAndApplyButton();
		return entryTable;
		
	}

	/*
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		//Initialize the preference store we wish to use
		setPreferenceStore(BbPlugin.getDefault().getPreferenceStore());
	}

	/**
	 * Performs special processing when this page's Restore Defaults button has been pressed.
	 * Sets the contents of the nameEntry field to
	 * be the default 
	 */
	protected void performDefaults() {

	}
	/** 
	 * Method declared on IPreferencePage. Save the
	 * SDK root path to the preference store.
	 */
	public boolean performOk() {
		BbPlugin.getDefault().setSdkRootDirPath(this.sdkRootText.getText());
		return true;
	}
	


}