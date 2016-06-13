/*
 * Copyright Swinburne University of Technology, 2011.
 * 
 */

package org.oscelot.bb.stripes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Wiley Fuller <wfuller@swin.edu.au>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoginRequired {

}
