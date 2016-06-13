package ${basePackage}.stripes;

import java.util.ArrayList;
import java.util.List;

import blackboard.servlet.data.MultiSelectBean;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;


public class DataCollectionPageAction implements ActionBean {

	private ActionBeanContext context;
	/** This is the collection of Options that will be displayed on the "Source" side
	 * of the multi select list on dataCollectionPage.jsp.
	 */
	private List<MultiSelectBean> multiSelectOptions;	

	private List<CatalogItem> catalogItems;	
	
	
	@DefaultHandler
	public Resolution displayDataCollectionPage() {
		
		// Put some values in the Multi select options
		multiSelectOptions = new ArrayList<MultiSelectBean>();
		MultiSelectBean selectOption1 = new MultiSelectBean();
		selectOption1.setValue("option1");
		selectOption1.setLabel("Option One");
		MultiSelectBean selectOption2 = new MultiSelectBean();
		selectOption2.setValue("option2");
		selectOption2.setLabel("Option Two");
		MultiSelectBean selectOption3 = new MultiSelectBean();
		selectOption3.setValue("option3");
		selectOption3.setLabel("Option Three");
		multiSelectOptions.add(selectOption1);
		multiSelectOptions.add(selectOption2);
		multiSelectOptions.add(selectOption3);
		
		
		catalogItems = new ArrayList<DataCollectionPageAction.CatalogItem>();
		
		CatalogItem item = new CatalogItem();
		item.setCatNum("1");
		item.setName("Catalog Item One");
		catalogItems.add(item);
		
		
		return new ForwardResolution("/dataCollectionPage.jsp");
	}

	@Override
	public ActionBeanContext getContext() {
		return context;
	}

	@Override
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}

	public void setMultiSelectOptions(List<MultiSelectBean> multiSelectOptions) {
		this.multiSelectOptions = multiSelectOptions;
	}

	public List<MultiSelectBean> getMultiSelectOptions() {
		return multiSelectOptions;
	}
	
	public void setCatalogItems(List<CatalogItem> catalogItems) {
		this.catalogItems = catalogItems;
	}

	public List<CatalogItem> getCatalogItems() {
		return catalogItems;
	}

	public class CatalogItem {
		private String catNum;
		private String name;
		public void setCatNum(String catNum) {
			this.catNum = catNum;
		}
		public String getCatNum() {
			return catNum;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
	
}
