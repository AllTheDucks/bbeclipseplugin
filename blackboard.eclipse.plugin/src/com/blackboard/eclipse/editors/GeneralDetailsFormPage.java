package com.blackboard.eclipse.editors;

import java.util.StringTokenizer;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.jdom.IDOMNode;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField.ColumnsDescription;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ufacekit.core.databinding.instance.IInstanceObservedContainer;
import org.eclipse.ufacekit.core.databinding.instance.InstanceObservables;
import org.eclipse.ufacekit.core.databinding.instance.observable.ILazyObserving;
import org.eclipse.ufacekit.core.databinding.sse.dom.DOMModelInstanceObservedContainer;
import org.eclipse.ufacekit.core.databinding.sse.dom.SSEDOMObservables;
import org.eclipse.ufacekit.core.databinding.sse.dom.xpath.XPathObserving;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IManagedForm;

import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.Form;
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
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.NodeList;

import com.blackboard.eclipse.BbPlugin;
import com.blackboard.eclipse.HelpContextIds;
import com.blackboard.eclipse.editors.internal.BbManifestObserving;
import com.blackboard.eclipse.editors.internal.BbManifestXPathObserving;
import com.blackboard.eclipse.editors.internal.XmlDocBinder;

public class GeneralDetailsFormPage extends FormPage implements IResourceChangeListener {

	private static final String VENDOR_ID_TOO_LONG_ERROR_KEY = "VENDOR_ID_TOO_LONG_ERROR";
	private static final String HANDLE_TOO_LONG_ERROR_KEY = "HANDLE_TOO_LONG_ERROR";
	private static final String NAME_TOO_LONG_ERROR_KEY = "HANDLE_TOO_LONG_ERROR";
	
	protected static final int VENDOR_ID_MAX_LENGTH = 4;
	protected static final int HANDLE_MAX_LENGTH = 32;
	protected static final int NAME_MAX_LENGTH = 50;
	
	/**
	 * @param id
	 * @param title
	 */
	private final BbManifestMultiPageEditor manifestEditor;
	private IDOMModel model;

	IInstanceObservedContainer container;

	Text handleText;
	Text nameText;
	Text descText;
	Text versionText;
	Combo bbVersionCombo;

	Text vendorHandleText;
	Text vendorNameText;
	Text vendorUrlText;
	Text vendorDescText;

	Text configUrlText;
	Text removeUrlText;
	
	ModifyListener titleListener;

	public GeneralDetailsFormPage(FormEditor editor) {
		super(editor, "second", Messages.generalPage_label); //$NON-NLS-1$ //$NON-NLS-2$
		this.manifestEditor = (BbManifestMultiPageEditor) editor;
		
		titleListener = new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent arg0) {
				
				String title;
				String vh = (vendorHandleText.getText() == null || vendorHandleText.getText().equals("")) ?"?":vendorHandleText.getText();
				String appHandle = (handleText.getText() == null || handleText.getText().equals(""))?"?":handleText.getText();
				if ((vendorHandleText.getText() == null || vendorHandleText.getText().equals("") )
						&& (handleText.getText() == null || handleText.getText().equals(""))) {
					title = "bb-manifest.xml";
				} else {
					title = vh + "-" + appHandle;
				}
				manifestEditor.setEditorTitle(title);
				
			}
		};
		
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		IFile file = ((IFileEditorInput) input).getFile();
		try {
			this.model = getDOMModel(file);
		} catch (Exception e) {
			throw new RuntimeException("Invalid Input: Must be DOM", e);
		}
		((IDOMElement)this.model.getDocument().getDocumentElement()).addAdapter(new XmlDocBinder());
//		model.addModelStateListener(new XmlDocBinder());
	}

	// Get DOM Model
	private IDOMModel getDOMModel(IFile file) throws Exception {

		IModelManager manager = StructuredModelManager.getModelManager();
		IStructuredModel model = manager.getExistingModelForRead(file);
		if (model == null) {
			model = manager.getModelForRead(file);
		}
		if (model == null) {
			throw new Exception(
					"DOM Model is null, check the content type of your file (it appears that it's not *.xml file)");
		}
		if (!(model instanceof IDOMModel)) {
			throw new Exception("Model retrieved is not a DOM Model!!!");
		}

		// Add listener to observe change of DOM (change is done with another
		// editor).
		model.addModelStateListener(listener);

		// model.setReinitializeNeeded(true);

		return (IDOMModel) model;
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		// Remove listener
		this.model.removeModelStateListener(listener);
		if (model.isDirty()) {
			// The DOM is changed and this editor was closed without save it
			// try {
			// //model.reinit();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			model.releaseFromRead();

			// model.getUndoManager().undo();
		}
		super.dispose();
		if (container != null)
			container.dispose();
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		
		ScrolledForm form = managedForm.getForm();
		PlatformUI.getWorkbench().getHelpSystem().setHelp(form, HelpContextIds.MANIFEST_OVERVIEW_PAGE);
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText(Messages.generalPage_label); //$NON-NLS-1$
		toolkit.decorateFormHeading(form.getForm());
		ImageDescriptor icon = AbstractUIPlugin.imageDescriptorFromPlugin(BbPlugin.PLUGIN_ID, "icons/b2-icon-16.png");
		form.setImage(icon.createImage());
		ColumnLayout layout = new ColumnLayout();
		layout.topMargin = 10;
		layout.bottomMargin = 5;
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;
		layout.maxNumColumns = 2;
		layout.minNumColumns = 2;
		
//		TableWrapLayout layout = new TableWrapLayout();
//		layout.numColumns = 2;
		form.getBody().setLayout(layout);
		ColumnLayoutData cd = new ColumnLayoutData();
		
		// form.getBody().setBackground(
		// form.getBody().getDisplay().getSystemColor(SWT.COLOR_GREEN));
		createVendorSection(managedForm, Messages.generalPage_vendor_title, Messages.generalPage_vendor_description, 2);
		createGeneralSection(managedForm, Messages.generalPage_general_title,
				Messages.generalPage_general_description, 2);
		createAdminUrlsSection(managedForm, Messages.generalPage_adminUrls_title, Messages.generalPage_adminUrls_description, 2);
		populateForm();
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
//		populateForm();
	}

	private void createGeneralSection(IManagedForm mform, String title, String desc, int nlinks) {
		Composite client = createSection(mform, title, desc, 1);
		FormToolkit toolkit = mform.getToolkit();
		GridLayout gl = new GridLayout(2, false);
		GridData gd;

		client.setLayout(gl);
		
		toolkit.createLabel(client, Messages.generalPage_general_handle_label);
		handleText = toolkit.createText(client, null, SWT.BORDER|SWT.SCROLL_LINE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		handleText.setLayoutData(gd);

		toolkit.createLabel(client, Messages.generalPage_general_name_label);
		nameText = toolkit.createText(client, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		nameText.setLayoutData(gd);
		nameText.addModifyListener(new FieldLengthVerifier(mform, NAME_TOO_LONG_ERROR_KEY, 
				Messages.generalPage_name_tooLong_errorMsg, NAME_MAX_LENGTH, nameText));

		
		handleText.addModifyListener(titleListener);
		handleText.addModifyListener(new FieldLengthVerifier(mform, HANDLE_TOO_LONG_ERROR_KEY, 
				Messages.generalPage_handle_tooLong_errorMsg, HANDLE_MAX_LENGTH, handleText));
		
		
		toolkit.createLabel(client, Messages.generalPage_general_desc_label);
		descText = toolkit.createText(client, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		descText.setLayoutData(gd);

		toolkit.createLabel(client, Messages.generalPage_general_version_label);
		versionText = toolkit.createText(client, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		versionText.setLayoutData(gd);

		toolkit.createLabel(client, Messages.generalPage_general_bbVer_label);
		bbVersionCombo = new Combo(client, SWT.BORDER);
		StringTokenizer st = new StringTokenizer(Messages.generalPage_general_bbVer_values, ",");
		while (st.hasMoreTokens()) {
			bbVersionCombo.add(st.nextToken());
		}
		// bbVersionCombo.setLayoutData(gd);


		
	}

	private void createVendorSection(IManagedForm mform, String title, String desc, int nlinks) {
		final IManagedForm form = mform;
		Composite client = createSection(mform, title, desc, 1);
		FormToolkit toolkit = mform.getToolkit();
		GridLayout gl = new GridLayout(2, false);
		GridData gd;
		
		gl.numColumns = 2;
		client.setLayout(gl);

		
		toolkit.createLabel(client, Messages.generalPage_vendor_id_label);
		vendorHandleText = toolkit.createText(client, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		vendorHandleText.setLayoutData(gd);
		vendorHandleText.addModifyListener(titleListener);
		vendorHandleText.addModifyListener(new FieldLengthVerifier(form, VENDOR_ID_TOO_LONG_ERROR_KEY, 
				Messages.generalPage_vendorId_tooLong_errorMsg, 4, vendorHandleText));

		toolkit.createLabel(client, Messages.generalPage_vendor_name_label);
		vendorNameText = toolkit.createText(client, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		vendorNameText.setLayoutData(gd);

		toolkit.createLabel(client, Messages.generalPage_vendor_url_label);
		vendorUrlText = toolkit.createText(client, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		vendorUrlText.setLayoutData(gd);

		toolkit.createLabel(client, Messages.generalPage_vendor_description_label);
		vendorDescText = toolkit.createText(client, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		vendorDescText.setLayoutData(gd);

	}

	private void createAdminUrlsSection(IManagedForm mform, String title, String desc, int nlinks) {
		Composite client = createSection(mform, title, desc, 1);
		FormToolkit toolkit = mform.getToolkit();
		GridLayout gl = new GridLayout(2, false);
		gl.numColumns = 2;
		GridData gd;
		client.setLayout(gl);

		toolkit.createLabel(client, Messages.generalPage_adminUrls_configUrl_label);
		configUrlText = toolkit.createText(client, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		configUrlText.setLayoutData(gd);

		toolkit.createLabel(client, Messages.generalPage_adminUrls_removeUrl_label);
		removeUrlText = toolkit.createText(client, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		removeUrlText.setLayoutData(gd);

	}

	private Composite createSection(IManagedForm mform, String title, String desc, int numColumns) {
		final ScrolledForm form = mform.getForm();
		FormToolkit toolkit = mform.getToolkit();
		Section section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION
				| Section.EXPANDED);
		section.setText(title);
		section.setDescription(desc);
		// toolkit.createCompositeSeparator(section);
		Composite client = toolkit.createComposite(section);

		section.setClient(client);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(false);
			}
		});
		return client;

	}

	public static IDOMElement getElement(IDOMElement base, String tagName) {
		NodeList list = base.getElementsByTagName(tagName);
		if (list.getLength() == 0) {
			return null;
		} else {
			return (IDOMElement) list.item(0);
		}
	}

	private void populateForm() {

		Realm realm = SWTObservables.getRealm(Display.getCurrent());
		DataBindingContext context = new DataBindingContext();
		container = new DOMModelInstanceObservedContainer(model, this);


		IObserving handleObserving = null;
		IObserving nameObserving = null;
		IObserving descriptionObserving = null;
		IObserving versionObserving = null;
		IObserving bbVersionObserving = null;
		IObserving bbMaxVersionObserving = null;

		IObserving vendorHandleObserving = null;
		IObserving vendorNameObserving = null;
		IObserving vendorUrlObserving = null;
		IObserving vendorDescObserving = null;

		IObserving configUrlObserving = null;
		IObserving removeUrlObserving = null;

		try {
			handleObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/handle", true);
			nameObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/name", true);
			descriptionObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/description", true);
			versionObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/version", true);
			bbVersionObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/requires/bbversion",true);
			bbMaxVersionObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/requires/bbversion", true);

			vendorHandleObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/vendor/id", true);
			vendorNameObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/vendor/name", true);
			vendorUrlObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/vendor/url", true);
			vendorDescObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/vendor/description", true);

			configUrlObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/http-actions/config", true);
			removeUrlObserving = new BbManifestXPathObserving(model.getDocument(), "manifest/plugin/http-actions/remove", true);
			
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IObservableValue handleObservable = InstanceObservables.observeValue(container, handleObserving);
		context.bindValue(SWTObservables.observeText(handleText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, handleObservable, "value"), null, null);

		IObservableValue nameObservable = InstanceObservables.observeValue(container, nameObserving);
		context.bindValue(SWTObservables.observeText(nameText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, nameObservable, "value"), null, null);

		IObservableValue descriptionObservable = InstanceObservables.observeValue(container, descriptionObserving);
		context.bindValue(SWTObservables.observeText(descText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, descriptionObservable, "value"), null, null);

		IObservableValue versionObservable = InstanceObservables.observeValue(container, versionObserving);
		context.bindValue(SWTObservables.observeText(versionText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, versionObservable, "value"), null, null);

		IObservableValue bbVersionObservable = InstanceObservables.observeValue(container, bbVersionObserving);
		context.bindValue(SWTObservables.observeSelection(bbVersionCombo),
				SSEDOMObservables.observeDetailAttrValue(realm, bbVersionObservable, "value"), null, null);


		// Vendor Details
		IObservableValue vendorHandleObservable = InstanceObservables.observeValue(container, vendorHandleObserving);
		context.bindValue(SWTObservables.observeText(vendorHandleText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, vendorHandleObservable, "value"), null, null);

		IObservableValue vendorNameObservable = InstanceObservables.observeValue(container, vendorNameObserving);
		context.bindValue(SWTObservables.observeText(vendorNameText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, vendorNameObservable, "value"), null, null);

		IObservableValue vendorUrlObservable = InstanceObservables.observeValue(container, vendorUrlObserving);
		context.bindValue(SWTObservables.observeText(vendorUrlText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, vendorUrlObservable, "value"), null, null);

		IObservableValue vendorDescriptionObservable = InstanceObservables.observeValue(container, vendorDescObserving);
		context.bindValue(SWTObservables.observeText(vendorDescText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, vendorDescriptionObservable, "value"), null, null);

		IObservableValue configUrlObservable = InstanceObservables.observeValue(container, configUrlObserving);
		context.bindValue(SWTObservables.observeText(configUrlText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, configUrlObservable, "value"), null, null);

		IObservableValue removeUrlObservable = InstanceObservables.observeValue(container, removeUrlObserving);
		context.bindValue(SWTObservables.observeText(removeUrlText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, removeUrlObservable, "value"), null, null);

	}

	// Listener used to observe change of DOM (change is done with another
	// editor).
	private IModelStateListener listener = new IModelStateListener() {

		public void modelAboutToBeChanged(IStructuredModel model) {
			// System.out.println("******* modelAboutToBeChanged *******");
			// System.out.println(model);
			// System.out.println("*************************************");
		}

		public void modelAboutToBeReinitialized(IStructuredModel model) {
			// System.out.println("******* modelAboutToBeReinitialized *******");
			// System.out.println(model);
			// System.out.println("*******************************************");

		}

		public void modelChanged(IStructuredModel model) {
			// System.out.println("******* modelChanged *******");
			// System.out.println(model);
			// System.out.println("****************************");

			// UPdate UI From DOM Model which have changed
			// updateUIFromDOMModel();
		}

		public void modelDirtyStateChanged(IStructuredModel model, boolean isDirty) {
			// System.out.println("******* modelDirtyStateChanged *******");
			// System.out.println(model);
			// System.out.println("**************************************");

			// dirty from DOM Model has changed (the XML content was changed
			// with anothher editor), fire the dirty property change to
			// indicate to the editor that dirty has changed.
			firePropertyChange(IEditorPart.PROP_DIRTY);

		}

		public void modelReinitialized(IStructuredModel model) {
			// System.out.println("******* modelReinitialized *******");
			// System.out.println(model);
			// System.out.println("**********************************");

		}

		public void modelResourceDeleted(IStructuredModel model) {
			// System.out.println("******* modelResourceDeleted *******");
			// System.out.println(model);
			// System.out.println("************************************");

		}

		public void modelResourceMoved(IStructuredModel oldModel, IStructuredModel newModel) {
			// System.out.println("******* modelResourceMoved *******");
			// System.out.println(oldModel);
			// System.out.println(newModel);
			// System.out.println("************************************");
		}

	};

	@Override
	public void resourceChanged(IResourceChangeEvent arg0) {
		// TODO Auto-generated method stub
		// Don't do anything at this stage.

	}
	
	private class FieldLengthVerifier implements ModifyListener {

		IManagedForm form;
		String messageKey;
		String errorMsg;
		int maxLength;
		Text textField;
		
		public FieldLengthVerifier(IManagedForm form, String messageKey, 
				String errorMsg, int maxLength, Text textField) {
			this.form = form;
			this.messageKey = messageKey;
			this.errorMsg = errorMsg;
			this.maxLength = maxLength;
			this.textField = textField;
		}
		
		@Override
		public void modifyText(ModifyEvent event) {
			if (textField.getText().length()>maxLength) {
				form.getMessageManager().addMessage(messageKey, 
						errorMsg, null, 
						IMessage.ERROR, textField);
			} else {
				form.getMessageManager().removeMessage(messageKey, textField);
			}
		}
		
	}

}
