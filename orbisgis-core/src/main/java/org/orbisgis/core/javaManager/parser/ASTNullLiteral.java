/* Generated By:JJTree: Do not edit this line. ASTNullLiteral.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.core.javaManager.parser;

public class ASTNullLiteral extends SimpleNode {
	public ASTNullLiteral(int id) {
		super(id);
	}

	public ASTNullLiteral(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=278d474ec3581e12ce728123efff7240 (do not edit this
 * line)
 */
