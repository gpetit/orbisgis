/* Generated By:JJTree: Do not edit this line. ASTBlock.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.core.javaManager.parser;

public class ASTBlock extends SimpleNode {
	public ASTBlock(int id) {
		super(id);
	}

	public ASTBlock(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=4b6f8a394afbafe104a370c64b50ebdb (do not edit this
 * line)
 */
