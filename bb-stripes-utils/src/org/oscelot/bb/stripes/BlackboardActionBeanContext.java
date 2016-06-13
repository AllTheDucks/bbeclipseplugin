/*
 * Copyright Swinburne University of Technology, 2011.
 * 
 */
package org.oscelot.bb.stripes;

import blackboard.data.course.Course;
import blackboard.data.user.User;
import blackboard.persist.Id;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;
import net.sourceforge.stripes.action.ActionBeanContext;

/**
 *
 * @author Wiley Fuller <wfuller@swin.edu.au>
 */

public class BlackboardActionBeanContext extends ActionBeanContext {
    
    Context context;
    
    public Context getBlackboardContext() {
        if (context == null) {
            context = ContextManagerFactory.getInstance().getContext();
        }
        return context;
    }
    
    public Id getCourseId() {
        return getBlackboardContext().getCourseId();
    }
    
    public Course getCourse() {
        return getBlackboardContext().getCourse();
    }
    
    public Id getUserId() {
        return getBlackboardContext().getUserId();
    }
    
    public User getUser() {
        return getBlackboardContext().getUser();
    }
}
