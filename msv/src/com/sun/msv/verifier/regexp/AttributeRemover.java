package com.sun.tranquilo.verifier.regexp;

import com.sun.tranquilo.grammar.*;

/**
 * Creates an expression whose AttributeExp is completely replaced by epsilon.
 * 
 * This step is used to erase all unconsumed AttributeExp from the expression.
 * This class is used for error recovery. Usually, unconsumed attributes are
 * violation of validity.
 */
public class AttributeRemover extends ExpressionCloner
{
	protected AttributeRemover( ExpressionPool pool ) { super(pool); }
	
	public Expression onAttribute( AttributeExp exp )	{ return Expression.epsilon; }
	public Expression onRef( ReferenceExp exp )			{ return exp.exp.visit(this); }
}