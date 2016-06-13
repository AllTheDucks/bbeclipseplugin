package com.blackboard.eclipse.editors;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.crypto.NodeSetData;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.ant.internal.ui.WorkbenchAntRunner;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.dom.events.DOMEventConstants;
import org.eclipse.core.dom.utils.DOMEventUtils;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ufacekit.core.databinding.instance.IInstanceObservedContainer;
import org.eclipse.ufacekit.core.databinding.instance.InstanceObservables;
import org.eclipse.ufacekit.core.databinding.instance.observable.ILazyObserving;
import org.eclipse.ufacekit.core.databinding.sse.dom.DOMModelInstanceObservedContainer;
import org.eclipse.ufacekit.core.databinding.sse.dom.SSEDOMObservables;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
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
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import com.blackboard.eclipse.BbPlugin;
import com.blackboard.eclipse.editors.internal.BbManifestXPathObserving;

public class AppDefsFormPage extends FormPage {

	BbManifestMultiPageEditor editor;
	DataBindingContext context;
	IInstanceObservedContainer container;

	/*
	 * TODO fields for the application section. .. need to move these into
	 * another class at some point
	 */
	Text handleText;
	Text nameText;
	Text descriptionText;
	Text smallIconText;
	Text largeIconText;
	Combo typeCombo;

	Button useSslButton;
	Button canAllowGuestButton;
	Button isCourseToolButton;
	Button isGroupToolButton;
	Button isOrgToolButton;
	Button isSysToolButton;

	XPath xpath;
	String expression = "manifest/plugin/application-defs/application[1]";
	XPathExpression expr;

	// Link Detail Controls
	Table linkElementsTable;
	TableViewer linkElementsTableViewer;

	Combo linkTypeCombo;
	Text linkNameText;
	Text linkUrlText;
	Text linkDescText;
	Text toolbarIconText;
	Text listitemIconText;

	// Link Detail Bindings
	AutoFillArrayList linkTypeObserving;
	AutoFillArrayList linkTypeObservable;
	AutoFillArrayList linkTypeDetailObservable;
	Binding linkTypeBinding;
	AutoFillArrayList linkNameObserving;
	AutoFillArrayList linkNameObservable;
	AutoFillArrayList linkNameDetailObservable;
	Binding linkNameBinding;
	AutoFillArrayList linkUrlObserving;
	AutoFillArrayList linkUrlObservable;
	AutoFillArrayList linkUrlDetailObservable;
	Binding linkUrlBinding;
	AutoFillArrayList linkDescriptionObserving;
	AutoFillArrayList linkDescriptionObservable;
	AutoFillArrayList linkDescriptionDetailObservable;
	Binding linkDescriptionBinding;
	IObserving linkListitemIconObserving;
	AutoFillArrayList<IObservableValue> linkToolbarIconDetailObservable;
	Binding linkToolbarIconBinding;
	AutoFillArrayList<IObservableValue> linkListitemIconDetailObservable;
	Binding linkListitemIconBinding;


	/**
	 * @param id
	 * @param title
	 */
	public AppDefsFormPage(FormEditor editor) {
		super(editor, "second", "Applications"); //$NON-NLS-1$ //$NON-NLS-2$
		this.editor = (BbManifestMultiPageEditor) editor;
		linkTypeObserving = new AutoFillArrayList(20);
		linkTypeObservable = new AutoFillArrayList(20);
		linkTypeDetailObservable = new AutoFillArrayList(20);
		linkNameObserving = new AutoFillArrayList(20);
		linkNameObservable = new AutoFillArrayList(20);
		linkNameDetailObservable = new AutoFillArrayList(20);
		linkUrlObserving = new AutoFillArrayList(20);
		linkUrlObservable = new AutoFillArrayList(20);
		linkUrlDetailObservable = new AutoFillArrayList(20);
		linkDescriptionObserving = new AutoFillArrayList(20);
		linkDescriptionObservable = new AutoFillArrayList(20);
		linkDescriptionDetailObservable = new AutoFillArrayList(20);
		linkToolbarIconDetailObservable = new AutoFillArrayList<IObservableValue>(20);
		linkListitemIconDetailObservable = new AutoFillArrayList<IObservableValue>(20);
	}

	protected void createFormContent(IManagedForm managedForm) {

		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText(Messages.appDefsPage_label); 
		toolkit.decorateFormHeading(form.getForm());
		ImageDescriptor icon = AbstractUIPlugin.imageDescriptorFromPlugin(BbPlugin.PLUGIN_ID, "icons/b2-icon-16.png");
		form.setImage(icon.createImage());
		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 1;
		layout.minNumColumns = 1;
		form.getBody().setLayout(layout);
		Section section = createApplicationSection(managedForm);
		// section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bindForm();
		editor.getModel().addModelStateListener(modelListener);
		
	
	}


	private Section createApplicationSection(IManagedForm mform) {
		final ScrolledForm form = mform.getForm();

		FormToolkit toolkit = mform.getToolkit();

		Section appSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION
				| Section.EXPANDED
		// | Section.TWISTIE //TODO This will be needed for multiple application
		// sections. .. right?
				);
		appSection.setText(Messages.appDefsPage_application_title);
		appSection.setDescription(Messages.appDefsPage_application_description);
		// toolkit.createCompositeSeparator(section);
		Composite appBody = toolkit.createComposite(appSection);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 1;
		appBody.setLayout(layout);
		appSection.setClient(appBody);
		appSection.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(false);
			}
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		Composite topArea = toolkit.createComposite(appBody);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		topArea.setLayoutData(gd);
		layout = new GridLayout(2, true);
		topArea.setLayout(layout);

		Composite topLeftArea = toolkit.createComposite(topArea);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		topLeftArea.setLayoutData(gd);
		layout = new GridLayout();
		layout.numColumns = 2;
		topLeftArea.setLayout(layout);

		Composite topRightArea = toolkit.createComposite(topArea);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		topRightArea.setLayoutData(gd);
		layout = new GridLayout();
		layout.numColumns = 2;
		topRightArea.setLayout(layout);

		toolkit.createLabel(topLeftArea, Messages.appDefsPage_application_handle_label);
		handleText = toolkit.createText(topLeftArea, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		handleText.setLayoutData(gd);

		toolkit.createLabel(topLeftArea, Messages.appDefsPage_application_name_label);
		nameText = toolkit.createText(topLeftArea, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		nameText.setLayoutData(gd);

		toolkit.createLabel(topLeftArea, Messages.appDefsPage_application_description_label);
		descriptionText = toolkit.createText(topLeftArea, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		descriptionText.setLayoutData(gd);



		toolkit.createLabel(topLeftArea, null);
		useSslButton = toolkit.createButton(topLeftArea, Messages.appDefsPage_application_useSsl_label, SWT.CHECK);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		useSslButton.setLayoutData(gd);

		toolkit.createLabel(topLeftArea, null);
		canAllowGuestButton = toolkit.createButton(topLeftArea, Messages.appDefsPage_application_canAllowGuest_label,
				SWT.CHECK);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		canAllowGuestButton.setLayoutData(gd);
		
		
		// Now add items to the right hand area...
		toolkit.createLabel(topRightArea, Messages.appDefsPage_application_smallIcon_label);
		smallIconText = toolkit.createText(topRightArea, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		smallIconText.setLayoutData(gd);

		toolkit.createLabel(topRightArea, Messages.appDefsPage_application_largeIcon_label);
		largeIconText = toolkit.createText(topRightArea, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		largeIconText.setLayoutData(gd);

		toolkit.createLabel(topRightArea, Messages.appDefsPage_application_type_label);
		typeCombo = new Combo(topRightArea, SWT.BORDER);
		StringTokenizer st = new StringTokenizer(Messages.appDefsPage_application_type_values, ",");
		while (st.hasMoreTokens()) {
			typeCombo.add(st.nextToken());
		}
		gd = new GridData(GridData.FILL_HORIZONTAL);
		
		//Create a composite to put the checkboxes in...
		toolkit.createLabel(topRightArea, null);
		Composite checkboxArea = toolkit.createComposite(topRightArea);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		topRightArea.setLayoutData(gd);
		layout = new GridLayout();
		layout.numColumns = 2;
		checkboxArea.setLayout(layout);
		
		//is-xxx-tool checkboxes
		isCourseToolButton = toolkit.createButton(checkboxArea, Messages.appDefsPage_application_isCourseTool_label,
				SWT.CHECK);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		isCourseToolButton.setLayoutData(gd);
		
		isGroupToolButton = toolkit.createButton(checkboxArea, Messages.appDefsPage_application_isGroupTool_label,
				SWT.CHECK);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		isGroupToolButton.setLayoutData(gd);
		
		isOrgToolButton = toolkit.createButton(checkboxArea, Messages.appDefsPage_application_isOrgTool_label,
				SWT.CHECK);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		isOrgToolButton.setLayoutData(gd);
		
		isSysToolButton = toolkit.createButton(checkboxArea, Messages.appDefsPage_application_isSysTool_label,
				SWT.CHECK);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		isSysToolButton.setLayoutData(gd);
		
		ToolTypeConflictListener toolTypeConflictListener = new ToolTypeConflictListener(form.getForm());
		
		isSysToolButton.addSelectionListener(toolTypeConflictListener);
		isOrgToolButton.addSelectionListener(toolTypeConflictListener);
		isCourseToolButton.addSelectionListener(toolTypeConflictListener);
		isGroupToolButton.addSelectionListener(toolTypeConflictListener);
		typeCombo.addSelectionListener(toolTypeConflictListener);
		typeCombo.addModifyListener(toolTypeConflictListener);
		
		Composite bottomArea = toolkit.createComposite(appBody);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		bottomArea.setLayoutData(gd);
		layout = new GridLayout(2, true);
		bottomArea.setLayout(layout);

		Section bottomLeftSection = toolkit.createSection(bottomArea, Section.TITLE_BAR | Section.EXPANDED);
		bottomLeftSection.setText("Links");
		Composite bottomLeftArea = toolkit.createComposite(bottomLeftSection);
		bottomLeftSection.setClient(bottomLeftArea);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		bottomLeftSection.setLayoutData(gd);
		layout = new GridLayout();
		layout.numColumns = 2;
		bottomLeftArea.setLayout(layout);

		Section bottomRightSection = toolkit.createSection(bottomArea, Section.TITLE_BAR | Section.EXPANDED);
		bottomRightSection.setText(Messages.appDefsPage_linkMaster_label);
		Composite bottomRightArea = toolkit.createComposite(bottomRightSection);
		bottomRightSection.setClient(bottomRightArea);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		bottomRightSection.setLayoutData(gd);
		layout = new GridLayout();
		layout.numColumns = 2;
		bottomRightArea.setLayout(layout);

		linkElementsTable = toolkit.createTable(bottomLeftArea, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 200;
		gd.widthHint = 100;
		linkElementsTable.setLayoutData(gd);

		Composite buttonArea = toolkit.createComposite(bottomLeftArea);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		buttonArea.setLayoutData(gd);
		layout = new GridLayout(1, false);
		buttonArea.setLayout(layout);
		Button addButton = toolkit.createButton(buttonArea, Messages.appDefsPage_linkMaster_add_label, 0);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		Button removeButton = toolkit.createButton(buttonArea, Messages.appDefsPage_linkMaster_remove_label, 0);

		toolkit.createLabel(bottomRightArea, Messages.appDefsPage_linkDetail_type_label);
		linkTypeCombo = new Combo(bottomRightArea, SWT.BORDER);
		// gd = new GridData(GridData.FILL_HORIZONTAL);
		// linkTypeCombo.setLayoutData(gd);
		st = new StringTokenizer(Messages.appDefsPage_linkDetail_type_values, ",");
		while (st.hasMoreTokens()) {
			linkTypeCombo.add(st.nextToken());
		}

		toolkit.createLabel(bottomRightArea, Messages.appDefsPage_linkDetail_name_label);
		linkNameText = toolkit.createText(bottomRightArea, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		linkNameText.setLayoutData(gd);

		toolkit.createLabel(bottomRightArea, Messages.appDefsPage_linkDetail_url_label);
		linkUrlText = toolkit.createText(bottomRightArea, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		linkUrlText.setLayoutData(gd);

		toolkit.createLabel(bottomRightArea, Messages.appDefsPage_linkDetail_description_label);
		linkDescText = toolkit.createText(bottomRightArea, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		linkDescText.setLayoutData(gd);

		toolkit.createLabel(bottomRightArea, Messages.appDefsPage_linkDetail_toolbarIcon_label);
		toolbarIconText = toolkit.createText(bottomRightArea, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		toolbarIconText.setLayoutData(gd);

		toolkit.createLabel(bottomRightArea, Messages.appDefsPage_linkDetail_listitemIcon_label);
		listitemIconText = toolkit.createText(bottomRightArea, null, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		listitemIconText.setLayoutData(gd);

		linkElementsTableViewer = new TableViewer(linkElementsTable);
		linkElementsTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				Element appElement = getAppElement();
				int selectedIndex = ((TableViewer) event.getSource()).getTable().getSelectionIndex();
				if (selectedIndex >= 0) {
					// selected index may be -1 in some cases... if an element disappears ??
				    bindLinkDetails(selectedIndex);
				}
			}
		});
		linkElementsTableViewer.setLabelProvider(new LinksListLabelProvider());
		linkElementsTableViewer.setContentProvider(new LinksListContentProvider());
		linkElementsTableViewer.setInput(editor.getEditorInput());

		addButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				Element linksElement = (Element)getAppElement().getElementsByTagName("links").item(0);
				linksElement.appendChild(linksElement.getOwnerDocument().createElement("link"));
				linkElementsTableViewer.refresh();
				linkElementsTable.setSelection(linkElementsTable.getItemCount()-1);
				bindLinkDetails(linkElementsTable.getItemCount()-1);
				linkTypeCombo.select(0);
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		
		removeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				Element linksElement = (Element)getAppElement().getElementsByTagName("links").item(0);
				
				int selectedElementIndex = linkElementsTable.getSelectionIndex();
				NodeList linkElements = linksElement.getElementsByTagName("link");
				if (selectedElementIndex >= 0) {
					linksElement.removeChild(linkElements.item(selectedElementIndex));
					unbindLinkDetails();
					// Remove the observers
					linkTypeObserving.set(selectedElementIndex, null); 
					clearLinkDetails();
					disableLinkDetails();
				}
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		
		return appSection;
	}

	private void bindForm() {
		Realm realm = SWTObservables.getRealm(Display.getCurrent());
		context = new DataBindingContext();
		container = new DOMModelInstanceObservedContainer(editor.getModel(), this);

		IObserving handleObserving = null;
		IObserving nameObserving = null;
		IObserving descriptionObserving = null;
		IObserving smallIconObserving = null;
		IObserving largeIconObserving = null;
		IObserving typeComboObserving = null;

		try {
			handleObserving = new BbManifestXPathObserving(editor.getModel().getDocument(),
					"manifest/plugin/application-defs/application[1]", true);
			nameObserving = new BbManifestXPathObserving(editor.getModel().getDocument(),
					"manifest/plugin/application-defs/application[1]", true);
			descriptionObserving = new BbManifestXPathObserving(editor.getModel().getDocument(),
					"manifest/plugin/application-defs/application[1]/description", true);
			smallIconObserving = new BbManifestXPathObserving(editor.getModel().getDocument(),
					"manifest/plugin/application-defs/application[1]", true);
			largeIconObserving = new BbManifestXPathObserving(editor.getModel().getDocument(),
					"manifest/plugin/application-defs/application[1]", true);
			typeComboObserving = new BbManifestXPathObserving(editor.getModel().getDocument(),
					"manifest/plugin/application-defs/application[1]", true);

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IObservableValue handleObservable = InstanceObservables.observeValue(container, handleObserving);
		context.bindValue(SWTObservables.observeText(handleText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, handleObservable, "handle"), null, null);

		IObservableValue nameObservable = InstanceObservables.observeValue(container, nameObserving);
		context.bindValue(SWTObservables.observeText(nameText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, nameObservable, "name"), null, null);

		IObservableValue descriptionObservable = InstanceObservables.observeValue(container, descriptionObserving);
		context.bindValue(SWTObservables.observeText(descriptionText, SWT.Modify),
				SSEDOMObservables.observeDetailCharacterData(realm, descriptionObservable), null, null);

		IObservableValue smallIconObservable = InstanceObservables.observeValue(container, descriptionObserving);
		context.bindValue(SWTObservables.observeText(smallIconText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, smallIconObservable, "small-icon"), null, null);

		IObservableValue largeIconObservable = InstanceObservables.observeValue(container, largeIconObserving);
		context.bindValue(SWTObservables.observeText(largeIconText, SWT.Modify),
				SSEDOMObservables.observeDetailAttrValue(realm, largeIconObservable, "large-icon"), null, null);

		IObservableValue typeObservable = InstanceObservables.observeValue(container, typeComboObserving);
		context.bindValue(SWTObservables.observeSelection(typeCombo),
				SSEDOMObservables.observeDetailAttrValue(realm, typeObservable, "type"), null, null);

		XPath xpath = XPathFactory.newInstance().newXPath();
		String expression = "manifest/plugin/application-defs/application[1]";
		XPathExpression expr;
		Element appElement = null;
		try {
			expr = xpath.compile(expression);
			appElement = (Element) expr.evaluate(editor.getModel().getDocument(), XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// update the use-ssl and can-allow-guest checkboxes
		Boolean useSsl = appElement == null?true:Boolean.parseBoolean(appElement.getAttribute("use-ssl"));
		Boolean canAllowGuest = appElement == null?true:Boolean.parseBoolean(appElement.getAttribute("can-allow-guest"));
		Boolean isCourseTool = appElement == null?true:Boolean.parseBoolean(appElement.getAttribute("is-sys-tool"));
		Boolean isGroupTool = appElement == null?true:Boolean.parseBoolean(appElement.getAttribute("is-course-tool"));
		Boolean isOrgTool = appElement == null?true:Boolean.parseBoolean(appElement.getAttribute("is-group-tool"));
		Boolean isSysTool = appElement == null?true:Boolean.parseBoolean(appElement.getAttribute("is-org-tool"));
		
		useSslButton.setSelection(useSsl);
		canAllowGuestButton.setSelection(canAllowGuest);
		isCourseToolButton.setSelection(isCourseTool);
		isOrgToolButton.setSelection(isOrgTool);
		isGroupToolButton.setSelection(isGroupTool);
		isSysToolButton.setSelection(isSysTool);
		
		useSslButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				Element appElement = getAppElement();
				appElement.setAttribute("use-ssl", Boolean.toString(((Button) evt.getSource()).getSelection()));
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		canAllowGuestButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				Element appElement = getAppElement();
				appElement.setAttribute("can-allow-guest", Boolean.toString(((Button) evt.getSource()).getSelection()));
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		isCourseToolButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				Element appElement = getAppElement();
				appElement.setAttribute("is-course-tool", Boolean.toString(((Button) evt.getSource()).getSelection()));
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		isOrgToolButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				Element appElement = getAppElement();
				appElement.setAttribute("is-org-tool", Boolean.toString(((Button) evt.getSource()).getSelection()));
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		isGroupToolButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				Element appElement = getAppElement();
				appElement.setAttribute("is-group-tool", Boolean.toString(((Button) evt.getSource()).getSelection()));
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		isSysToolButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				Element appElement = getAppElement();
				appElement.setAttribute("is-sys-tool", Boolean.toString(((Button) evt.getSource()).getSelection()));
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		disableLinkDetails();
	}

	class LinksListContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			Element appElement = getAppElement();
			if (appElement == null) {
				return new Object[0];
			}
			NodeList linkElements = appElement.getElementsByTagName("link");
			Object[] tableElements = new Object[linkElements.getLength()];
			for (int i = 0; i < linkElements.getLength(); i++) {
				tableElements[i] = linkElements.item(i);
			}
			return tableElements;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class LinksListLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			Element typeNode = (Element) ((Element) obj).getElementsByTagName("type").item(0);
			Element nameNode = (Element) ((Element) obj).getElementsByTagName("name").item(0);
			String type = typeNode == null ? "" : typeNode.getAttribute("value"); 
			String name = nameNode == null ? "" : nameNode.getAttribute("value");
			return type + " - " + name;
		}

		public Image getColumnImage(Object obj, int index) {

			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
		}
	}

	private void unbindLinkDetails() {
		if (linkTypeBinding != null) {
			linkTypeBinding.dispose();
			context.removeBinding(linkTypeBinding);
			linkTypeBinding = null;
			linkNameBinding.dispose();
			context.removeBinding(linkNameBinding);
			linkNameBinding = null;
			linkUrlBinding.dispose();
			context.removeBinding(linkUrlBinding);
			linkUrlBinding = null;
			linkDescriptionBinding.dispose();
			context.removeBinding(linkDescriptionBinding);
			linkDescriptionBinding = null;
			linkToolbarIconBinding.dispose();
			context.removeBinding(linkToolbarIconBinding);
			linkToolbarIconBinding = null;
			linkListitemIconBinding.dispose();
			context.removeBinding(linkListitemIconBinding);
			linkListitemIconBinding = null;
		} 
	}
	
	private void bindLinkDetails(int linkIndex) {
		Element linkElement = (Element) getAppElement().getElementsByTagName("link").item(linkIndex);

		Realm realm = SWTObservables.getRealm(Display.getCurrent());

		enableLinkDetails();
		try {
			if (linkTypeObserving.size()<= linkIndex || linkTypeObserving.get(linkIndex) == null) {
				linkTypeObserving.set(linkIndex, new BbManifestXPathObserving(editor.getModel().getDocument(),
						"manifest/plugin/application-defs/application[1]/links/link[" + (linkIndex + 1) + "]/type",true));
				linkTypeObservable.set(linkIndex, InstanceObservables.observeValue(container, (IObserving)linkTypeObserving.get(linkIndex)));
				linkTypeDetailObservable.set(linkIndex,
						SSEDOMObservables.observeDetailAttrValue(realm, (IObservableValue)linkTypeObservable.get(linkIndex), "value"));
				
				linkNameObserving.set(linkIndex, new BbManifestXPathObserving(editor.getModel().getDocument(),
						"manifest/plugin/application-defs/application[1]/links/link[" + (linkIndex + 1) + "]/name",true));
				linkNameObservable.set(linkIndex, InstanceObservables.observeValue(container, (IObserving)linkNameObserving.get(linkIndex)));
				linkNameDetailObservable.set(linkIndex,SSEDOMObservables.observeDetailAttrValue(realm, (IObservableValue)linkNameObservable.get(linkIndex), "value"));
				
				linkDescriptionObserving.set(linkIndex, new BbManifestXPathObserving(editor.getModel().getDocument(),
						"manifest/plugin/application-defs/application[1]/links/link[" + (linkIndex + 1)+ "]/description", true));
				linkDescriptionObservable.set(linkIndex, InstanceObservables.observeValue(container, (IObserving)linkDescriptionObserving.get(linkIndex)));
				linkDescriptionDetailObservable.set(linkIndex,
						SSEDOMObservables.observeDetailAttrValue(realm, (IObservableValue)linkDescriptionObservable.get(linkIndex), "value"));
				
				linkUrlObserving.set(linkIndex, new BbManifestXPathObserving(editor.getModel().getDocument(),
						"manifest/plugin/application-defs/application[1]/links/link[" + (linkIndex + 1) + "]/url", true));
				linkUrlObservable.set(linkIndex, InstanceObservables.observeValue(container, (IObserving)linkUrlObserving.get(linkIndex)));
				linkUrlDetailObservable.set(linkIndex,
						SSEDOMObservables.observeDetailAttrValue(realm, (IObservableValue)linkUrlObservable.get(linkIndex), "value"));
				
				IObserving linkListitemIconObserving = new BbManifestXPathObserving(editor.getModel().getDocument(),
						"manifest/plugin/application-defs/application[1]/links/link[" + (linkIndex + 1) + "]/icons/listitem", true);
				linkListitemIconDetailObservable.set(linkIndex, SSEDOMObservables.observeDetailAttrValue(realm, 
						InstanceObservables.observeValue(container, linkListitemIconObserving), "value"));

				IObserving linkToolbarIconObserving = new BbManifestXPathObserving(editor.getModel().getDocument(),
						"manifest/plugin/application-defs/application[1]/links/link[" + (linkIndex + 1) + "]/icons/toolbar", true);
				linkToolbarIconDetailObservable.set(linkIndex, SSEDOMObservables.observeDetailAttrValue(realm, 
						InstanceObservables.observeValue(container, linkToolbarIconObserving), "value"));
				
			}
		} catch (XPathExpressionException ex) {
			ex.printStackTrace();
		}
		
		unbindLinkDetails();

		linkTypeBinding = context.bindValue(SWTObservables.observeSelection(linkTypeCombo),
				(IObservableValue)linkTypeDetailObservable.get(linkIndex), null, null);

		linkNameBinding = context.bindValue(SWTObservables.observeText(linkNameText, SWT.Modify),
				(IObservableValue)linkNameDetailObservable.get(linkIndex), null, null);

		linkDescriptionBinding = context.bindValue(SWTObservables.observeText(linkDescText, SWT.Modify),
				(IObservableValue)linkDescriptionDetailObservable.get(linkIndex), null, null);

		linkUrlBinding = context.bindValue(SWTObservables.observeText(linkUrlText, SWT.Modify),
				 (IObservableValue)linkUrlDetailObservable.get(linkIndex), null, null);

		linkToolbarIconBinding = context.bindValue(SWTObservables.observeText(toolbarIconText, SWT.Modify),
				 (IObservableValue)linkToolbarIconDetailObservable.get(linkIndex), null, null);

		linkListitemIconBinding = context.bindValue(SWTObservables.observeText(listitemIconText, SWT.Modify),
				 (IObservableValue)linkListitemIconDetailObservable.get(linkIndex), null, null);
		
		linkTypeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				linkElementsTableViewer.refresh();
			}
		});
		linkNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				linkElementsTableViewer.refresh();
			}
		});
		
	}

	private Element getAppElement() {
		Element appElement = null;
		if (xpath == null) {
			xpath = XPathFactory.newInstance().newXPath();

			try {
				expr = xpath.compile(expression);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			appElement = (Element) expr.evaluate(editor.getModel().getDocument(), XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
		return appElement;
	}

	private void disableLinkDetails() {
		linkTypeCombo.setEnabled(false);
		linkNameText.setEnabled(false);
		linkUrlText.setEnabled(false);
		linkDescText.setEnabled(false);
		toolbarIconText.setEnabled(false);
		listitemIconText.setEnabled(false);
	}

	private void enableLinkDetails() {
		linkTypeCombo.setEnabled(true);
		linkNameText.setEnabled(true);
		linkUrlText.setEnabled(true);
		linkDescText.setEnabled(true);
		toolbarIconText.setEnabled(true);
		listitemIconText.setEnabled(true);
	}

	private void clearLinkDetails() {
		linkTypeCombo.setText("");
		linkNameText.setText("");
		linkUrlText.setText("");
		linkDescText.setText("");
		toolbarIconText.setText("");
		listitemIconText.setText("");
	}


	private class AutoFillArrayList<E> extends ArrayList<E> {
		public AutoFillArrayList(int i) {
			super(i);
		}

		@Override
		public E set(int index, E element) {
			if (size() <= index) {
				for (int i = size()-1; i < index; i++) {
					super.add(null);
				}
			}
			return super.set(index, element);
		}
	}
	
	IModelStateListener modelListener = new IModelStateListener() {
		
		@Override
		public void modelResourceMoved(IStructuredModel arg0, IStructuredModel arg1) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void modelResourceDeleted(IStructuredModel arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void modelReinitialized(IStructuredModel arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void modelDirtyStateChanged(IStructuredModel arg0, boolean arg1) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void modelChanged(IStructuredModel arg0) {
			Element appElement = getAppElement();
			Element linksElement = (Element) appElement.getElementsByTagName("links").item(0);
			if (linksElement != null) {
				int elementCount = linksElement.getElementsByTagName("link").getLength();
				if (elementCount > 0) {
					if (elementCount != linkElementsTable.getItemCount()) {
						linkElementsTableViewer.refresh();
					}
				}
			}
			
			
			// update the use-ssl and can-allow-guest checkboxes
			Boolean useSsl = Boolean.parseBoolean(appElement.getAttribute("use-ssl"));
			Boolean canAllowGuest = Boolean.parseBoolean(appElement.getAttribute("can-allow-guest"));
			
			useSslButton.setSelection(useSsl);
			canAllowGuestButton.setSelection(canAllowGuest);
		}
		
		@Override
		public void modelAboutToBeReinitialized(IStructuredModel arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void modelAboutToBeChanged(IStructuredModel arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	class ToolTypeConflictListener implements SelectionListener, ModifyListener {
		
		Form form;
		
		public ToolTypeConflictListener(Form form) {
			this.form = form;
		}

		@Override
		public void modifyText(ModifyEvent arg0) {
			checkForConflicts();
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			checkForConflicts();
		}
		@Override
		public void widgetSelected(SelectionEvent event) {
			checkForConflicts();
		}
		public void checkForConflicts() {
			if (!typeCombo.getText().equals("") && (isSysToolButton.getSelection() || isOrgToolButton.getSelection()
						|| isGroupToolButton.getSelection() || isCourseToolButton.getSelection())) {
					form.setMessage("If a value is set in the 'type' combo, the values" +
							"of the is-xxx-tool checkboxes will be ignored.", IMessage.WARNING);

			} else {
				form.setMessage(null);
			}
		}
		
	}
}