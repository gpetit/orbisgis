/* Generated By:JJTree: Do not edit this line. ASTConstructorDeclaration.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.core.javaManager.parser;

public class ASTConstructorDeclaration extends SimpleNode {
	public ASTConstructorDeclaration(int id) {
		super(id);
	}

	public ASTConstructorDeclaration(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=a1368f991e41264a3c0dc9c5b5e852da (do not edit this
 * line)
 */
