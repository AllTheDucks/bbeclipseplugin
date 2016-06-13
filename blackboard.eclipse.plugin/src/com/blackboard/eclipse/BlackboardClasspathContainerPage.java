package com.blackboard.eclipse;



import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import com.blackboard.eclipse.sdk.BlackboardSdk;

public class BlackboardClasspathContainerPage extends WizardPage implements IClasspathContainerPage,
		IClasspathContainerPageExtension {

	private Combo bbVersionList;
	private IJavaProject project;
	private IClasspathEntry currentClasspathEntry;
	private String version;
	
	public BlackboardClasspathContainerPage() {
	        super("Blackboard Server Libraries", "Blackboard Server Libraries", null);
	        setDescription("Select the version of the Blackboard libraries you wish to compile your building block against.");
	        setPageComplete(true);
	}
	@Override
	public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
                | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setFont(parent.getFont());
        
    	
		bbVersionList = new Combo(composite, SWT.BORDER|SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		bbVersionList.setLayoutData(gd);
		
		java.util.List<BlackboardSdk> sdks = BbPlugin.getDefault().getSdkManager().getAllSdks();
		if (sdks != null) {
			int i = 0;
			for (BlackboardSdk sdk : sdks) {
				bbVersionList.add(sdk.getVersion());
				if (sdk.getVersion().equals(version)){
					bbVersionList.select(i);
				}
				i++;
			}
		}    
		
        setControl(composite);

	}

	@Override
	public void initialize(IJavaProject project, IClasspathEntry[] entries) {
		this.project = project;

	}

	@Override
	public boolean finish() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public IClasspathEntry getSelection() {
		String selectedVersion = bbVersionList.getItem(bbVersionList.getSelectionIndex()); 
		IPath path = new Path(BlackboardClasspathContainer.CONTAINER_ID+IPath.SEPARATOR+selectedVersion);
		return JavaCore.newContainerEntry(path);
	}

	@Override
	public void setSelection(IClasspathEntry classpathEntry) {
		if (classpathEntry != null) {
			IPath path = classpathEntry.getPath();
			version = path.removeFirstSegments(1).toString();
			currentClasspathEntry = classpathEntry;
		}
	}

}
