/* Generated By:JJTree: Do not edit this line. ASTBlockStatement.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.core.javaManager.parser;

public class ASTBlockStatement extends SimpleNode {
	public ASTBlockStatement(int id) {
		super(id);
	}

	public ASTBlockStatement(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=5d3800c67165d1b9d40b6831c2a15d2e (do not edit this
 * line)
 */
