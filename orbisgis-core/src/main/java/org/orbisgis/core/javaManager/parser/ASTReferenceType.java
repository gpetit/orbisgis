/* Generated By:JJTree: Do not edit this line. ASTReferenceType.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.core.javaManager.parser;

public class ASTReferenceType extends SimpleNode {
	public ASTReferenceType(int id) {
		super(id);
	}

	public ASTReferenceType(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=2dee630ce9b75088cf266f575338fa1f (do not edit this
 * line)
 */
