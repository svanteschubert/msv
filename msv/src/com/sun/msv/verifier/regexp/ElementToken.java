/*
 * @(#)$Id$
 *
 * Copyright 2001 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package com.sun.tranquilo.verifier.regexp;

import com.sun.tranquilo.grammar.ElementExp;

/**
 * a token that represents an XML element.
 * 
 * @author <a href="mailto:kohsuke.kawaguchi@eng.sun.com">Kohsuke KAWAGUCHI</a>
 */
public class ElementToken extends Token
{
	final ElementExp[] acceptedPatterns;
	
	public ElementToken( ElementExp[] acceptedPatterns )
	{
		this.acceptedPatterns = acceptedPatterns;
	}
	
	boolean match( ElementExp exp )
	{
		// since every subpatterns are reused, object identity is enough
		// to judge the equality of patterns
		for( int i=0; i<acceptedPatterns.length; i++ )
			if( acceptedPatterns[i]==exp )	return true;
		return false;
	}
}
