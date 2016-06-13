package ${basePackage}.stripes;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;


public class ConfigAction implements ActionBean {

	private ActionBeanContext context;
	
	
	@DefaultHandler
	public Resolution displayConfigPage() {
		return new ForwardResolution("/WEB-INF/jsp/config.jsp");
	}

	@Override
	public ActionBeanContext getContext() {
		return context;
	}

	@Override
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}
	
}
