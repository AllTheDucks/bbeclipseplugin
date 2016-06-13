package com.blackboard.eclipse.wizards;

import java.awt.Dialog;
import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.forms.widgets.Hyperlink;

import com.blackboard.eclipse.BbPlugin;
import com.blackboard.eclipse.HelpContextIds;
import com.blackboard.eclipse.ProjectTemplate;
import com.blackboard.eclipse.sdk.BlackboardSdk;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class BuildingBlockNewWizardPage extends WizardPage {
	private Text vendorIdText;
	private boolean vendorIdTouched = false;
	private Text pluginIdText;
	private boolean pluginIdTouched = false;
	private Text packageText;
	boolean packageNameValid;
	
	private Text serverText;
	private Text projectLocationText;
	private Text pluginNameText;
	private boolean pluginNameTouched = false;
	private Text pluginDescText;
	private boolean pluginDescTouched = false;
	private Group locationArea;
	private IProject project;
	
	private Combo bbVersionList;
	private String selectedVersion;
	
	Combo bbTemplateList;
	java.util.List<ProjectTemplate> templates;
	
	private String projectName;
	private String projectPath;
	private String workspaceRoot;
	private Button defaultPathRadio;
	private Button otherPathRadio;
	private Composite dirGroup;
	
	String rootSdkPath;
	
	private boolean useDefaultLocation = true;

	private ISelection selection;
	private boolean warningIsSet;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public BuildingBlockNewWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Blackboard Building Block");
		setDescription("This wizard creates a new simple Building Block Project.");
		this.selection = selection;
		
	}
	

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HelpContextIds.NEW_PROJECT_WIZARD_PAGE);
		
		rootSdkPath = BbPlugin.getDefault().getSdkRootDirPath();

		FormData fd1 = new FormData();
			
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 3;
		Label vendorIdLabel = new Label(container, SWT.NULL);
		
		vendorIdLabel.setText("Vendor Id:");

		setVendorIdText(new Text(container, SWT.BORDER | SWT.SINGLE));
		vendorIdText.setTextLimit(4);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		getVendorIdText().setLayoutData(gd);
		getVendorIdText().addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateProjectName();
				updateLocation();
				vendorIdTouched = true;
				validatePage();
			}
		});
		
		getVendorIdText().setFocus();
		
		Label pluginIdLabel = new Label(container, SWT.NULL);
		pluginIdLabel.setText("&Plugin Handle: (e.g. my-courses, custom-tool, video-player, etc)");

		setPluginIdText(new Text(container, SWT.BORDER | SWT.SINGLE));
		pluginIdText.setTextLimit(32);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		getPluginIdText().setLayoutData(gd);
		getPluginIdText().addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateProjectName();
				updateLocation();
				pluginIdTouched = true;
				validatePage();
			}
		});
		
		Label packageLabel = new Label(container, SWT.NULL);
		packageLabel.setText("&Base Package: (e.g. edu.myinstitution.mytool)");

		packageText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		packageText.setLayoutData(gd);

		packageText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent evt) {
				validatePage();
			}
		});
		
		Label serverLabel = new Label(container, SWT.NULL);
		serverLabel.setText("&Development Server: (e.g. 127.0.0.1)");

		serverText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		serverText.setLayoutData(gd);
		
		Label bbVersionLabel = new Label(container, SWT.NULL);
		bbVersionLabel.setText("Target Blackboard Version");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		bbVersionLabel.setLayoutData(gd);
		Composite bbVersionArea = new Composite(container, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		bbVersionArea.setLayoutData(gd);
		GridLayout bbVersionAreaLayout = new GridLayout(2,false);
		bbVersionArea.setLayout(bbVersionAreaLayout);
		
		bbVersionList = new Combo(bbVersionArea, SWT.BORDER|SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		bbVersionList.setLayoutData(gd);

		Link setSdkDirLink = new Link(bbVersionArea, SWT.NULL);
		setSdkDirLink.setText("<A>Set SDK root dir.</A>");
		
		final Shell parentShell = this.getShell();

		
		setSdkDirLink.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				
				//TODO This code block is a copy of the BbPluginSdkPreferencePage code. Needs refactoring.
				final Shell dialog = new Shell(parentShell, SWT.DIALOG_TRIM);
				
				dialog.setSize(350, 300);
				dialog.setText("Set SDK Root Directory");
				
				dialog.setLayout(new GridLayout());
				
				//Add in a dummy label for spacing
				Label desc = new Label(dialog,SWT.NONE|SWT.WRAP);
				desc.setText("Select the directory containing the blackboard SDKs. "+
						"An SDK is a collection of JAR files from the Blackboard systemlib directory. "+
						"Suggested Location is <Your Home Directory>/bb-sdk/");
				
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				desc.setLayoutData(gd);
//				desc.setBounds(x, y, width, height)
				//				desc.setSize(100, 100);
//				desc.setData(data);
				
				
				Composite buttonComposite = new Composite(dialog,SWT.NULL);
				
				GridLayout buttonLayout = new GridLayout();
				buttonLayout.numColumns = 2;
				buttonComposite.setLayout(buttonLayout);

				//Create a data that takes up the extra space in the dialog and spans both columns.
				gd = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING);
				buttonComposite.setLayoutData(gd);		
				
				
				final Text sdkRootText = new Text(buttonComposite, SWT.BORDER|SWT.READ_ONLY);
				//Create a data that takes up the extra space in the dialog .
				gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.grabExcessHorizontalSpace = true;
				sdkRootText.setLayoutData(gd);


				Composite bottomButtonBar = new Composite(dialog, SWT.NULL);
				bottomButtonBar.setLayout(new GridLayout(2, false));
				
				final Button rootDirOkButton = new Button(bottomButtonBar, SWT.PUSH | SWT.CENTER);
				rootDirOkButton.setText("Continue");
				rootDirOkButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						BbPlugin.getDefault().setSdkRootDirPath(rootSdkPath);
						populateBbVersionList();
						dialog.close();
					}
				});
				File sdkDir = new File(rootSdkPath);
				if (rootSdkPath != null && sdkDir.exists() && sdkDir.isDirectory()) {
					rootDirOkButton.setEnabled(true);
				} else {
					rootDirOkButton.setEnabled(false);
				}
				
				Button rootDirCancelButton = new Button(bottomButtonBar, SWT.PUSH | SWT.CENTER);
				rootDirCancelButton.setText("Cancel");
				rootDirCancelButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						dialog.close();
					}
				});
				
				Button browseButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);

				final Shell parentShell = dialog;
				browseButton.setText("Browse..."); //$NON-NLS-1$
				browseButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						DirectoryDialog dirDialog = new DirectoryDialog(parentShell);
						sdkRootText.setText(dirDialog.open());
						File sdkDir = new File(sdkRootText.getText());
						if (sdkRootText.getText() != null && sdkDir.exists() && sdkDir.isDirectory()) {
							rootSdkPath = sdkRootText.getText();
							rootDirOkButton.setEnabled(true);
						}
					}
				});
				sdkRootText.setText(BbPlugin.getDefault().getSdkRootDirPath());				
				
				
				dialog.open();
			}
		});
		
		populateBbVersionList();
		
		bbVersionList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedVersion = bbVersionList.getItem(bbVersionList.getSelectionIndex());
			}
		});



		Label bbTemplateLabel = new Label(container, SWT.NULL);
		bbTemplateLabel.setText("Use Project Template");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		bbTemplateLabel.setLayoutData(gd);
		
		bbTemplateList = new Combo(container, SWT.BORDER|SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		bbTemplateList.setLayoutData(gd);
		
		templates = BbPlugin.getDefault().getTemplates();
		
		for (ProjectTemplate template : templates) {
			bbTemplateList.add(template.getName());
		}

		bbTemplateList.select(0);
		
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 3;
		
		locationArea = createLocationArea(container);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		locationArea.setLayoutData(gd);
		
		initialize();
		setControl(container);
	}

	private void updateLocation() {
		if (useDefaultLocation) {
			projectLocationText.setText(workspaceRoot+File.separator+projectName);
		}
	}
	
	private void populateBbVersionList() {
		java.util.List<BlackboardSdk> sdks = BbPlugin.getDefault().getSdkManager().getAllSdks();
		bbVersionList.removeAll();
		if (sdks == null || sdks.size() == 0) {
			sdks = BbPlugin.getDefault().getSdkManager().loadSdks(rootSdkPath);
		}
		if (sdks != null) {
			for (BlackboardSdk sdk : sdks) {
				bbVersionList.add(sdk.getVersion());
			}
		} else {
			validatePage();
		}
		if (bbVersionList.getItemCount() == 1) {
			bbVersionList.select(0);
			selectedVersion = bbVersionList.getItem(0);
		}
	}
	
	private Group createLocationArea(Composite container) {
		Group mainArea = new Group(container,SWT.NULL);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		mainArea.setLayout(layout);
		mainArea.setText("Location");
		
		defaultPathRadio = new Button(mainArea, SWT.RADIO);
		defaultPathRadio.setText("Create new project in workspace");
		defaultPathRadio.setSelection(true);
		defaultPathRadio.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				setDirGroupEnabled(false);
				useDefaultLocation = true;
				updateLocation();
				validatePage();
			}
			
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		otherPathRadio = new Button(mainArea, SWT.RADIO);
		otherPathRadio.setText("Create new project in:");
		otherPathRadio.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				setDirGroupEnabled(true);
				useDefaultLocation = false;
				validatePage();
				projectLocationText.setText("");
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		
		GridLayout dirGroupLayout = new GridLayout();
		dirGroupLayout.numColumns = 3;
		dirGroup = new Composite(mainArea,SWT.NULL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		dirGroup.setLayoutData(gd);
		dirGroup.setLayout(dirGroupLayout);
		
		Label label = new Label(dirGroup, SWT.NULL);
		label.setText("&Directory");
		projectLocationText = new Text(dirGroup, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		projectLocationText.setLayoutData(gd);
		workspaceRoot = Platform.getLocation().toString();
		projectLocationText.setText(workspaceRoot);
		projectLocationText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!useDefaultLocation ) {
					validatePage();
				}	
			}
		});		

		
		Button browseButton = new Button(dirGroup, SWT.NULL);
		browseButton.setText("Browse...");
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowse();
            }
		});
		setDirGroupEnabled(false);
				
		return mainArea;
	}
	
	private void setDirGroupEnabled(boolean enabled) {
		for (Control child: dirGroup.getChildren()) {
			child.setEnabled(enabled);
		}
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		setPageComplete(false);
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */
	
	private void updateProjectName() {
		if(getVendorIdText().getText() != null && getPluginIdText().getText() != null
				&& !getVendorIdText().getText().equals("") && !getPluginIdText().getText().equals("")) {
			projectName = getVendorIdText().getText() + "-" + getPluginIdText().getText();
		} else {
			projectName = "";
		}
	}

    /**
     * Display a directory browser and update the location path field with the selected path
     */
	private void handleBrowse() {

		String existing_dir = projectLocationText.getText();

		// Disable the path if it doesn't exist
		if (existing_dir.length() == 0) {
			existing_dir = null;
		} else {
			File f = new File(existing_dir);
			if (!f.exists()) {
				existing_dir = null;
			}
		}

		DirectoryDialog dd = new DirectoryDialog(projectLocationText.getShell());
		dd.setMessage("Browse for folder");
		dd.setFilterPath(existing_dir);
		String abs_dir = dd.open();

		if (abs_dir != null) {

			projectLocationText.setText(abs_dir);
			//	            validatePageComplete();
		}
	}
	

	
	private boolean validatePage() {
		String rootPath = BbPlugin.getDefault().getSdkRootDirPath();
		if (rootPath == null || rootPath.equals("")) {
			setErrorMessage("You must set the directory containing your Blackboard SDKs");
			return false;
		}
		
		setErrorMessage(null);
		projectPath = workspaceRoot + File.separator + getVendorIdText().getText()+ "-" + getPluginIdText().getText();

		project = ResourcesPlugin.getWorkspace().getRoot().getProject(getVendorIdText().getText()+ "-" + getPluginIdText().getText());
		if (vendorIdTouched && getVendorId().trim().length() == 0) {
			setMessage("Enter Vendor Id.");
			setPageComplete(false);
			return false;
		}
		if (pluginIdTouched && getPluginId().trim().length() == 0) {
			setMessage("Enter Plugin Id.");
			setPageComplete(false);
			return false;
		}
		
		if (bbTemplateList.getText() == null || bbTemplateList.getText().equals("")) {
			setErrorMessage("You must choose a project template.");
			return false;
		}
		
		if (vendorIdTouched && pluginIdTouched) {
			
			if (useDefaultLocation) {
				if(project.exists()) {
					setErrorMessage("Project already exists");
					setPageComplete(false);
					return false;
				}
			} else {
				IStatus status = ResourcesPlugin.getWorkspace().validateProjectLocation(null, new Path(projectLocationText.getText()));
				
				if (!status.isOK()) {
					setErrorMessage(status.getMessage());
					setPageComplete(false);
					return false;
				} else {
					setErrorMessage(null);
				}
			}
			setPageComplete(true);
		}

		String packageName = packageText.getText();
		if (packageName.length() != 0) {

			if (!packageName.matches("[a-zA-Z0-9._\\-\\$]*")) {
				setErrorMessage("Package Name contains Illegal Characters.");
				setPageComplete(false);
				return false;
			}

			char firstChar = packageName.charAt(0);
			char lastChar = packageName.charAt(packageName.length() - 1);
			if (firstChar == '.' || lastChar == '.') {
				setErrorMessage("Package can't begin or end with '.'");
				setPageComplete(false);
				return false;
			}

			String[] packageSegments = packageName.split("\\.");
			for (String segment : packageSegments) {
				char firstSegChar = segment.charAt(0);
				if (Character.isDigit(firstSegChar)) {
					setErrorMessage("Package can't begin with a number");
					setPageComplete(false);
					return false;
				}
			}
			for (String segment : packageSegments) {
				char firstSegChar = segment.charAt(0);
				if (Character.isUpperCase(firstSegChar)) {
					setMessage("Package shouldn't begin with an uppercase letter.", this.WARNING);
					return true;
				}
			}
		}
		
		setMessage(null);
		return true;
	}
	
	
	
	

	private void updateStatus(String message) {
		setErrorMessage(message);
	}

	public String getVendorId() {
		return getVendorIdText().getText();
	}

	public String getPluginId() {
		return getPluginIdText().getText();
	}
	
	public String getPluginName() {
		return pluginNameText.getText();
	}
	
	public String getPluginDesc() {
		return pluginDescText.getText();
	}
	
	public String getPackage() {
		return packageText.getText();
	}
	
	public String getServer() {
		return serverText.getText();
	}
	
	public String getProjectTemplateId() {
		return templates.get(bbTemplateList.getSelectionIndex()).getId();
	}
	
	public IProject getProject() {
		return project;
	}

	public String getBbVersion() {
		return selectedVersion;
	}


	public void setPluginIdText(Text pluginIdText) {
		this.pluginIdText = pluginIdText;
	}


	public Text getPluginIdText() {
		return pluginIdText;
	}


	public void setVendorIdText(Text vendorIdText) {
		this.vendorIdText = vendorIdText;
	}


	public Text getVendorIdText() {
		return vendorIdText;
	}
}