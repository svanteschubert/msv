package com.sun.msv.generator;

import org.w3c.dom.*;
import java.util.Set;
import java.util.Map;

public class DOMDecorator {
	private static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
	
	public static void decorate( Document doc ) {
		// collect namespace URIs that are used.
		final Set usedURIs = new java.util.HashSet();
		// also, collect prefixes that are already in use.
		final Set usedPrefixes = new java.util.HashSet();
		
		Element root = doc.getDocumentElement();
		
		visit( root, new DOMVisitor(){
			public void onElement( Element e ) {
				usedURIs.add(e.getNamespaceURI());
			}
			public void onAttr( Attr a ) {
				String uri = a.getNamespaceURI();
				if(!"".equals(uri) && !uri.equals(XMLNS_URI))
					usedURIs.add(a.getNamespaceURI());
				
				if(a.getNamespaceURI().equals(XMLNS_URI)) {
					String qname = a.getName();
					int idx = qname.indexOf(':');
					if(idx<0)
						usedPrefixes.add(qname.substring(idx+1));
				}
			}
		});
		
		// if an empty namespace "" is not used, then namespace URI of 
		// the root element is considered as the default namespace URI.
		final String defaultNs =
			(!usedURIs.contains(""))?
				root.getNamespaceURI():"";
		
		if( !defaultNs.equals("") )	// declare the default ns
			root.setAttributeNS( XMLNS_URI, "xmlns", defaultNs );
		
		// map from prefix to URI.
		final Map uri2prefix = new java.util.HashMap();
		
		// assign prefixes for other URIs.
		int cnt=1;
		String[] uris = (String[])usedURIs.toArray(new String[0]);
		for( int i=0; i<uris.length; i++ ) {
			
			String prefix;
			do {
				prefix = "ns"+(cnt++);
			} while( uri2prefix.containsValue(prefix) );
			
			uri2prefix.put( uris[i], prefix );
			// add this declaration to the document element.
			root.setAttributeNS( XMLNS_URI, "xmlns:"+prefix, uris[i] );
		}
		
		
		// visit nodes and set prefixes
		visit( root, new DOMVisitor(){
			public void onElement( Element e ) {
				if( e.getNamespaceURI().equals(defaultNs) )
					; // don't touch
				else
					e.setPrefix((String)uri2prefix.get(e.getNamespaceURI()));
			}
			public void onAttr( Attr a ) {
				String uri = a.getNamespaceURI();
				if("".equals(uri))
					return;	// do nothing.
				
				if(uri.equals(XMLNS_URI))
					return;	// don't touch xmlns declarations.
				
				String prefix = (String)uri2prefix.get(uri);
				a.setPrefix(prefix);
			}
		});
	}
	
	
	/** visits Elements and Attrs by using a DOMVisitor. */
	public static void visit( Element e, DOMVisitor visitor ) {
		visitor.onElement(e);
		// visit attributes
		NamedNodeMap map = e.getAttributes();
		for( int i=0; i<map.getLength(); i++ )
			visitor.onAttr( (Attr)map.item(i) );
		
		// visit children
		NodeList children = e.getChildNodes();
		for( int i=0; i<children.getLength(); i++ ) {
			Node n = children.item(i);
			if( n instanceof Element )
				visit((Element)n,visitor);
		}
	}
	
	static interface DOMVisitor {
		void onElement(Element e);
		void onAttr(Attr a);
	}
	
	
}
