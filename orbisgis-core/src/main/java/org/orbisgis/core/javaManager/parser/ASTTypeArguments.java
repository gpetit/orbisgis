/* Generated By:JJTree: Do not edit this line. ASTTypeArguments.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.core.javaManager.parser;

public class ASTTypeArguments extends SimpleNode {
	public ASTTypeArguments(int id) {
		super(id);
	}

	public ASTTypeArguments(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=1a570a8fb45556ae8d91e0c030140981 (do not edit this
 * line)
 */
