/* Generated By:JJTree: Do not edit this line. ASTInstanceOfExpression.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.core.javaManager.parser;

public class ASTInstanceOfExpression extends SimpleNode {
	public ASTInstanceOfExpression(int id) {
		super(id);
	}

	public ASTInstanceOfExpression(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=c3f1ec012f6605e8470d8a2c6cbc405e (do not edit this
 * line)
 */
