/* Generated By:JJTree: Do not edit this line. ASTPreDecrementExpression.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.core.javaManager.parser;

public class ASTPreDecrementExpression extends SimpleNode {
	public ASTPreDecrementExpression(int id) {
		super(id);
	}

	public ASTPreDecrementExpression(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=2d582f108b2658eddf71f18a8244161d (do not edit this
 * line)
 */
