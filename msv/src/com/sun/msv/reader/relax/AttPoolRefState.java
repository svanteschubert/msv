/*
 * @(#)$Id$
 *
 * Copyright 2001 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package com.sun.tranquilo.reader.relax;

import com.sun.tranquilo.grammar.Expression;
import com.sun.tranquilo.grammar.relax.AttPoolClause;
import com.sun.tranquilo.grammar.relax.RELAXModule;
import com.sun.tranquilo.reader.ExpressionWithoutChildState;

/**
 * parses &lt;ref role="..." /&gt;.
 * 
 * @author <a href="mailto:kohsuke.kawaguchi@eng.sun.com">Kohsuke KAWAGUCHI</a>
 */
public class AttPoolRefState extends ExpressionWithoutChildState
{
	protected Expression makeExpression()
	{
		final String role = startTag.getAttribute("role");
		
		// RELAXReader.resolveModuleReference cannot be used
		// because AttPool reference to the same module
		// and that to the different module must be treated differently
		
		final String namespace = startTag.getAttribute("namespace");
		
		final RELAXReader reader = (RELAXReader)this.reader;
		
		AttPoolClause c;
		
		if(namespace==null)	// reference to the same module
			c = reader.currentModule.attPools.getOrCreate(role);
		else	// to the different module
			c = reader.resolveModuleReference(startTag).exportedAttPools.getOrCreate(role);
		
		reader.backwardReference.memorizeLink( c, namespace!=null );
		return c;
	}
}
