package com.sun.tranquilo.reader.datatype.xsd;

import com.sun.tranquilo.reader.GrammarReader;
import com.sun.tranquilo.reader.IgnoreState;
import com.sun.tranquilo.reader.State;
import com.sun.tranquilo.datatype.BadTypeException;
import com.sun.tranquilo.datatype.DataType;
import com.sun.tranquilo.datatype.DataTypeFactory;
import com.sun.tranquilo.util.StartTagInfo;

public class ListState extends TypeWithOneChildState
{
	protected final String newTypeName;
	protected ListState( XSDVocabulary voc, String newTypeName )
	{
		super(voc);
		this.newTypeName = newTypeName;
	}
	
	protected DataType annealType( DataType baseType )
		throws BadTypeException
	{
		return DataTypeFactory.deriveByList( newTypeName, baseType );
	}
	
	protected void startSelf()
	{
		super.startSelf();
		
		// if itemType attribute is used, try to load it.
		String itemType = startTag.getAttribute("itemType");
		if(itemType!=null)
			type = reader.resolveDataType(itemType);
	}

	protected State createChildState( StartTagInfo tag )
	{
		// accepts elements from the same namespace only.
		if( !startTag.namespaceURI.equals(tag.namespaceURI) )	return null;
		
		if( tag.localName.equals("annotation") )	return new IgnoreState();
		if( tag.localName.equals("simpleType") )	return new SimpleTypeState(vocabulary);
		
		return null;	// unrecognized
	}
}