/* Generated By:JJTree: Do not edit this line. ASTTypeParameters.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.core.javaManager.parser;

public class ASTTypeParameters extends SimpleNode {
	public ASTTypeParameters(int id) {
		super(id);
	}

	public ASTTypeParameters(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=056d8c426cb3280f5c25628d0540cc05 (do not edit this
 * line)
 */
