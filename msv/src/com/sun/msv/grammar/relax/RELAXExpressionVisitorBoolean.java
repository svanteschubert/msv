/*
 * @(#)$Id$
 *
 * Copyright 2001 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package com.sun.tranquilo.grammar.relax;

import com.sun.tranquilo.grammar.ExpressionVisitorBoolean;

/**
 * RELAX version of {@link ExpressionVisitorBoolean}.
 * 
 * @author <a href="mailto:kohsuke.kawaguchi@eng.sun.com">Kohsuke KAWAGUCHI</a>
 */
public interface RELAXExpressionVisitorBoolean extends ExpressionVisitorBoolean
{
	// RELAX visitor can ignore onRef callback.
	boolean onAttPool( AttPoolClause exp );
	boolean onTag( TagClause exp );
	boolean onElementRules( ElementRules exp );
	boolean onHedgeRules( HedgeRules exp );
}
