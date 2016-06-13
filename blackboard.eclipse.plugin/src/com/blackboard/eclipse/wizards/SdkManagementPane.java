package com.blackboard.eclipse.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SdkManagementPane extends Composite {

	Shell parentShell;
	
	public SdkManagementPane(Composite parent, int style, Shell parentShell) {
		super(parent, style);
		this.parentShell = parentShell;
		init();
	}

	protected void init() {
	    this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    this.setLayout(new GridLayout(2,false));
	    List sdkList = new List(this, SWT.BORDER);
	    sdkList.add("Blackboard 8.0.375");
	    sdkList.add("Blackboard 9.1.456");
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL|GridData.GRAB_HORIZONTAL);
	    gd.heightHint = 200;
	    sdkList.setLayoutData(gd);
	    Composite rightPane = new Composite(this, SWT.NULL);
	    rightPane.setLayout(new GridLayout(1, true));
	    gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
	    rightPane.setLayoutData(gd);
	    
	    Button addSdkButton = new Button(rightPane, SWT.BORDER);
	    addSdkButton.setText("Add...");
	    gd = new GridData(GridData.FILL_HORIZONTAL);
	    addSdkButton.setLayoutData(gd);

	    Button removeSdkButton = new Button(rightPane, SWT.BORDER);
	    removeSdkButton.setText("Remove");

	    Button editSdkButton = new Button(rightPane, SWT.BORDER);
	    editSdkButton.setText("Edit...");
	    gd = new GridData(GridData.FILL_HORIZONTAL);
	    editSdkButton.setLayoutData(gd);
	    
	    addSdkButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				final Shell addDialog = new Shell(parentShell, SWT.APPLICATION_MODAL
					       | SWT.DIALOG_TRIM |SWT.RESIZE );
			    addDialog.setSize(350, 170);
			    addDialog.setLayout(new GridLayout(1,false));
			    
			    Composite dirDetailsGroup = new Composite(addDialog, SWT.NULL);
			    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			    dirDetailsGroup.setLayoutData(gd);
			    dirDetailsGroup.setLayout(new GridLayout(3,false));
			    Label libLocationLabel = new Label(dirDetailsGroup, SWT.NULL);
			    libLocationLabel.setText("BB Jars Location:");
			    final Text libLocationText = new Text(dirDetailsGroup, SWT.BORDER);
			    gd = new GridData(GridData.FILL_HORIZONTAL);
			    libLocationText.setLayoutData(gd);
			    Button libLocationButton = new Button(dirDetailsGroup, SWT.BORDER);
			    libLocationButton.setText("Browse...");
			    
			    libLocationButton.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						DirectoryDialog dirDialog = new DirectoryDialog(addDialog);
						libLocationText.setText(dirDialog.open());
					}
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
					}
				});

			    Label docLocationLabel = new Label(dirDetailsGroup, SWT.NULL);
			    docLocationLabel.setText("BB Docs Location:");
			    final Text docLocationText = new Text(dirDetailsGroup, SWT.BORDER);
			    gd = new GridData(GridData.FILL_HORIZONTAL);
			    docLocationText.setLayoutData(gd);
			    Button docLocationButton = new Button(dirDetailsGroup, SWT.BORDER);
			    docLocationButton.setText("Browse...");
			    docLocationButton.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						DirectoryDialog dirDialog = new DirectoryDialog(addDialog);
						docLocationText.setText(dirDialog.open());
					}
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
					}
				});
			    
			    
			    Composite bottomArea = new Composite(addDialog, SWT.NULL);
			    bottomArea.setLayout(new GridLayout(2,false));
			    Button okButton = new Button(bottomArea,SWT.BORDER);
			    okButton.setText("OK");
			    Button cancelButton = new Button(bottomArea, SWT.BORDER);
			    cancelButton.setText("Cancel");
			    
			    addDialog.open();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent evt) {
			}
		});
	    
	    
//	    Label libLocationLabel = new Label(rightPane, SWT.NULL);
//	    libLocationLabel.setText("Library Location:");
//	    Text libLocationText = new Text(rightPane, SWT.BORDER);
//	    gd = new GridData(GridData.FILL_HORIZONTAL);
//	    libLocationText.setLayoutData(gd);
//	    Button libLocBrowseButton = new Button(rightPane, SWT.BORDER);
//	    libLocBrowseButton.setText("Browse");
	}
	
}
