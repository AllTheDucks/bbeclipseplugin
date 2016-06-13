package com.blackboard.eclipse.editors;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.blackboard.eclipse.BbPlugin;

public class PermissionsFormPage extends FormPage {
	
	private BbManifestMultiPageEditor editor;
	private Table permissionsTable;
	private TableViewer tv;
	
	XPath xpath;
	String expression = "manifest/plugin/permissions";
	XPathExpression expr;

	/**
	 * @param id
	 * @param title
	 */
	public PermissionsFormPage(FormEditor editor) {
		super(editor, "second", "Permissions"); //$NON-NLS-1$ //$NON-NLS-2$
		this.editor = (BbManifestMultiPageEditor) editor;
		this.editor.getModel().addModelStateListener(modelListener);
	}

	protected void createFormContent(IManagedForm managedForm) {

		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Building Block Permissions"); //$NON-NLS-1$
		toolkit.decorateFormHeading(form.getForm());
		ImageDescriptor icon = AbstractUIPlugin.imageDescriptorFromPlugin(
				BbPlugin.PLUGIN_ID, "icons/b2-icon-16.png");
		form.setImage(icon.createImage());
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		form.getBody().setLayout(layout);
		
		Composite buttonArea = toolkit.createComposite(form.getBody());
		buttonArea.setLayout(new GridLayout(2, false));
		Button addButton = toolkit.createButton(buttonArea, "Add", 0);
		Button removeButton = toolkit.createButton(buttonArea, "Remove", 0);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		buttonArea.setLayoutData(gd);

		permissionsTable = toolkit.createTable(form.getBody(), SWT.BORDER);
		
		gd = new GridData(GridData.FILL_HORIZONTAL|GridData.FILL_VERTICAL);
		gd.heightHint = 200;
		gd.widthHint = 100;
		permissionsTable.setLayoutData(gd);
		
		
		tv = new TableViewer(permissionsTable);
		TableViewerColumn typeCol = new TableViewerColumn(tv, SWT.NONE,0);
		TableColumn column = typeCol.getColumn();
		column.setText("Type");
		column.setWidth(200);
		typeCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Element permissionElement = (Element)element;
				return permissionElement.getAttribute("type");
			}
		});
		typeCol.setEditingSupport(new TypeEditingSupport(tv));
		TableViewerColumn nameCol = new TableViewerColumn(tv, SWT.NONE);
		column = nameCol.getColumn();
		column.setText("name");
		column.setWidth(200);
		nameCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Element permissionElement = (Element)element;
				return permissionElement.getAttribute("name");
			}
		});
		nameCol.setEditingSupport(new NameEditingSupport(tv));
		TableViewerColumn actionsCol = new TableViewerColumn(tv, SWT.NONE);
		column = actionsCol.getColumn();
		column.setText("actions");
		column.setWidth(200);
		actionsCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Element permissionElement = (Element)element;
				return permissionElement.getAttribute("actions");
			}
		});
		actionsCol.setEditingSupport(new ActionsEditingSupport(tv));

		permissionsTable.setHeaderVisible(true);
		permissionsTable.setLinesVisible(true);
		
		tv.setContentProvider(new PermissionsListContentProvider());
		tv.setInput(editor.getEditorInput());

		tv.scrollDown(0, permissionsTable.getItemCount() * permissionsTable.getItemHeight());
		Element permissionsElement = getPermissionsElement();
		NodeList permissionElements = permissionsElement.getElementsByTagName("permission");

		addButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Element permissionsElement = getPermissionsElement();
				if (permissionsElement != null) {
					Element newElement = permissionsElement.getOwnerDocument().createElement("permission");
					permissionsElement.appendChild(newElement);
				}
				tv.refresh();
				tv.scrollUp(0, permissionsTable.getItemCount() * permissionsTable.getItemHeight());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		
		removeButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Element permissionsElement = getPermissionsElement();
				if (permissionsElement != null) {
					int selectedIndex = permissionsTable.getSelectionIndex();
					
					Node selectedNode = permissionsElement.getElementsByTagName("permission").item(selectedIndex);
					
					permissionsElement.removeChild(selectedNode);
				}
				tv.refresh();
				tv.scrollUp(0, permissionsTable.getItemCount() * permissionsTable.getItemHeight());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		
	}
	
	class PermissionsListContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			System.out.println("Getting elements for permission table.");
			Element permissionsElement = getPermissionsElement();
			NodeList linkElements = permissionsElement.getElementsByTagName("permission");
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
	
	private Section createPermissionsSection(IManagedForm mform) {
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
		return appSection;
	}
	private Element getPermissionsElement() {
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
	
	class TypeEditingSupport extends EditingSupport {

		private final TableViewer viewer;

		public TypeEditingSupport(TableViewer viewer) {
			super(viewer);
			this.viewer = viewer;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(viewer.getTable());
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected Object getValue(Object element) {
			return ((Element) element).getAttribute("type");
		}

		@Override
		protected void setValue(Object element, Object value) {
			((Element) element).setAttribute("type", String.valueOf(value));
			viewer.refresh();
		}
	}
	
	
	class NameEditingSupport extends EditingSupport {

		private final TableViewer viewer;

		public NameEditingSupport(TableViewer viewer) {
			super(viewer);
			this.viewer = viewer;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(viewer.getTable());
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected Object getValue(Object element) {
			return ((Element) element).getAttribute("name");
		}

		@Override
		protected void setValue(Object element, Object value) {
			((Element) element).setAttribute("name", String.valueOf(value));
			viewer.refresh();
		}
	}
	
	
	class ActionsEditingSupport extends EditingSupport {

		private final TableViewer viewer;

		public ActionsEditingSupport(TableViewer viewer) {
			super(viewer);
			this.viewer = viewer;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(viewer.getTable());
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected Object getValue(Object element) {
			return ((Element) element).getAttribute("actions");
		}

		@Override
		protected void setValue(Object element, Object value) {
			((Element) element).setAttribute("actions", String.valueOf(value));
			viewer.refresh();
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
			if (tv != null) {
				tv.refresh();
			}
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

}
