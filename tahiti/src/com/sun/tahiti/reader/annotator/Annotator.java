/*
 * @(#)$Id$
 *
 * Copyright 2001 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
package com.sun.tahiti.reader.annotator;

import com.sun.msv.grammar.Expression;
import com.sun.msv.reader.GrammarReader;
import com.sun.tahiti.grammar.util.*;

public class Annotator
{
	public static Expression annotate( Expression topLevel, GrammarReader reader ) {
		// remove <notAllowed/> from the grammar.
		topLevel = topLevel.visit( new NotAllowedRemover(reader.pool) );
		if( topLevel==Expression.nullSet )	return topLevel;
		
		// then remove temporarily added class items
		topLevel = topLevel.visit( new TemporaryClassItemRemover(reader.pool) );
		
		// performs field annotation.
		// this will normalize C-C/C-P/C-I relation and make up for missing FieldItems.
		topLevel = FieldItemAnnotation.annotate( topLevel, reader.pool );
		
		// then finally perform overall normalization.
		topLevel = RelationNormalizer.normalize( reader, topLevel );
		
		return topLevel;
	}
}