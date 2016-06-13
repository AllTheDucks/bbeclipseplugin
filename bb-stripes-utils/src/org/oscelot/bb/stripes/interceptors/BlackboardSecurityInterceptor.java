/*
 * Copyright Swinburne University of Technology, 2011.
 * 
 */
package org.oscelot.bb.stripes.interceptors;

import org.oscelot.bb.stripes.EntitlementRestrictions;
import org.oscelot.bb.stripes.LoginRequired;
import blackboard.data.user.User;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;
import blackboard.platform.security.Entitlement;
import blackboard.platform.security.SecurityUtil;
import blackboard.platform.session.BbSession;
import blackboard.platform.session.BbSessionManagerServiceFactory;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

/**
 *
 * A stripes interceptor for enforcing Blackboard security restrictions.<br>
 * Restrictions are based on blackboard Entitlements.
 *
 * @see EntitlementRestrictions
 * @see LoginRequired
 * @author Wiley Fuller <wfuller@swin.edu.au>
 */
@Intercepts({LifecycleStage.ActionBeanResolution, LifecycleStage.HandlerResolution})
public class BlackboardSecurityInterceptor implements Interceptor {

    @Override
    public Resolution intercept(ExecutionContext ctx) throws Exception {
        Logger logger = Logger.getLogger(BlackboardSecurityInterceptor.class.getName());
        logger.log(Level.FINE, "running the security interceptor for URL: {0} in lifecycle stage: {1}  And ActionBean: {2}", new Object[]{ctx.getActionBeanContext().getRequest().getRequestURI(), ctx.getLifecycleStage().name(), ctx.getActionBean() != null ? ctx.getActionBean().getClass().getName() : ""});

        Resolution resolution = ctx.proceed();
        ActionBean action = ctx.getActionBean();

        LoginRequired loginRequired = action.getClass().getAnnotation(LoginRequired.class);
        if (loginRequired != null) {
            BbSession bbSession = BbSessionManagerServiceFactory.getInstance().getSession(ctx.getActionBeanContext().getRequest());
            if (!bbSession.isAuthenticated()) {
                return new RedirectResolution("/", false);
            }
        }

        //Get entitlement restrictions from the Class
        EntitlementRestrictions actionBeanRestrictions = action.getClass().getAnnotation(EntitlementRestrictions.class);
        String errorPageUrl = null;
        if (actionBeanRestrictions != null) {
            errorPageUrl = actionBeanRestrictions.errorPage();
        }

        //Get entitlement restrictions from the handler Method
        Method handler = null;
        if (ctx.getLifecycleStage().equals(LifecycleStage.HandlerResolution)) {
            handler = ctx.getHandler();
        }
        EntitlementRestrictions handlerRestrictions = null;
        if (handler != null) {
            handlerRestrictions = handler.getAnnotation(EntitlementRestrictions.class);
        }
        if (handlerRestrictions != null) {
            errorPageUrl = handlerRestrictions.errorPage();
        }

        //Put them all in one array.
        int beanEntitlementsLength = (actionBeanRestrictions == null ? 0 : actionBeanRestrictions.entitlements().length);
        int handlerEntitlementsLength = (handlerRestrictions == null ? 0 : handlerRestrictions.entitlements().length);
        String[] allEntitlements = new String[beanEntitlementsLength + handlerEntitlementsLength];
        int eCounter = 0;
        for (int i = 0; eCounter < beanEntitlementsLength; eCounter++) {
            allEntitlements[eCounter] = actionBeanRestrictions.entitlements()[i++];
        }
        for (int i = 0; eCounter < allEntitlements.length; eCounter++) {
            allEntitlements[eCounter] = handlerRestrictions.entitlements()[i++];
        }

        //Check that the user has ALL the listed entitlements
        Context bbContext = ContextManagerFactory.getInstance().getContext();
        User user = bbContext.getUser();
        for (String entitlment : allEntitlements) {
            if (!SecurityUtil.userHasEntitlement(new Entitlement(entitlment))) {
                logger.log(Level.FINE, "Current User: {0} Doesn''t have entitlement: {1}", new Object[]{user.getUserName(), entitlment});
                return new RedirectResolution(errorPageUrl);
            }
        }
        return resolution;
    }
}
