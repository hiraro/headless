package net.umanohone.headless.parser;

import java.util.ArrayList;
import java.util.List;

import net.umanohone.headless.models.Element;
import net.umanohone.headless.models.ElementLocation;
import net.umanohone.headless.models.Method;
import net.umanohone.headless.models.Package;
import net.umanohone.headless.models.Type;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

public class ElementVisitor extends ASTVisitor {

	public static enum NodeType {
		METHOD(IBinding.METHOD, "Method"), TYPE(IBinding.TYPE, "Type"), PACKAGE(
				IBinding.PACKAGE, "Package"), VAL(IBinding.VARIABLE, "Variable"), UNDEF(
				null, "undefined"), LINE_COMMENT(ASTNode.LINE_COMMENT,
				"LineComment"), BLOCK_COMMENT(ASTNode.BLOCK_COMMENT,
				"BlockComment"), JAVADOC(ASTNode.JAVADOC, "Javadoc"), STRING_LITERAL(
				ASTNode.STRING_LITERAL, "StringLiteral"), NUMBER_LITERAL(
				ASTNode.NUMBER_LITERAL, "NumberLiteral");
		private String text;
		private Integer type;

		private NodeType(Integer type, String text) {
			this.text = text;
			this.type = type;
		}

		public String getText() {
			return text;
		}

		public int getType() {
			return type;
		}

		public static NodeType getByType(int type) {
			if (type == NodeType.METHOD.getType()) {
				return NodeType.METHOD;
			} else if (type == NodeType.TYPE.getType()) {
				return NodeType.TYPE;
			} else if (type == NodeType.PACKAGE.getType()) {
				return NodeType.PACKAGE;
			} else if (type == NodeType.VAL.getType()) {
				return NodeType.VAL;
			} else if (type == NodeType.LINE_COMMENT.getType()) {
				return NodeType.LINE_COMMENT;
			} else if (type == NodeType.BLOCK_COMMENT.getType()) {
				return NodeType.BLOCK_COMMENT;
			} else if (type == NodeType.JAVADOC.getType()) {
				return NodeType.JAVADOC;
			} else if (type == NodeType.STRING_LITERAL.getType()) {
				return NodeType.STRING_LITERAL;
			} else if (type == NodeType.NUMBER_LITERAL.getType()) {
				return NodeType.NUMBER_LITERAL;
			}
			return NodeType.UNDEF;
		}

	}

	private boolean visitMethod = false;
	private boolean inMethod = false;
	private CompilationUnit compilationUnit;
	private Package pakage;
	private Type type;
	private Method method;
	private ITypeBinding rawType;

	public ElementVisitor(CompilationUnit compilationUnit, Package pakage,
			Type type, Method method, ITypeBinding rawType) {
		super();
		this.compilationUnit = compilationUnit;
		this.pakage = pakage;
		this.type = type;
		this.method = method;
		this.rawType = rawType;
	}

	public boolean isVisitMethod() {
		return visitMethod;
	}

	public void setVisitMethod(boolean visitMethod) {
		this.visitMethod = visitMethod;
	}

	@Override
	public void endVisit(MethodDeclaration node) {
		this.inMethod = false;
		super.endVisit(node);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		this.inMethod = true;
		return super.visit(node);
	}

	@Override
	public boolean visit(SimpleName node) {
		if ((!visitMethod && inMethod) || (visitMethod && !inMethod)
				|| node.toString().length() < 3) {
			return super.visit(node);
		}
		ITypeBinding type = node.resolveTypeBinding();
		IBinding bind = node.resolveBinding();
		if (bind == null || type == null) {
			return super.visit(node);
		}
		Element elem = Element.findByKey(bind.getKey());
		if (elem == null) {
			elem = new Element();
			elem.setElementText(node.toString());
			int kind = bind.getKind();
			elem.setNodeKind(kind);
			elem.setNodeKindText(NodeType.getByType(kind).getText());
			elem.setElementKey(bind.getKey());
			elem.setTypeKey(type.getKey());
			elem.setTypeQualified(type.getQualifiedName());
			elem.setTypeText(type.getName());
			if (kind == NodeType.METHOD.getType()) {
				IMethodBinding methodBind = (IMethodBinding) bind;
				elem.setElementOption(methodBind.toString());
			}
		}

		elem = elem.save();
		ElementLocation location;
		location = new ElementLocation();
		location.setElement(elem);
		location.setPakage(this.pakage);
		location.setType(this.type);
		location.setMethod(this.method);
		IResource res = rawType.getJavaElement().getResource();
		if (res != null) {
			location.setFile(res.getRawLocationURI().toString());
			location.setOffset(node.getStartPosition());
			location.setLength(node.getLength());
			location.setLineEnd(compilationUnit.getLineNumber(node
					.getStartPosition() + node.getLength()));
			location.setLineStart(compilationUnit.getLineNumber(node
					.getStartPosition()));
		}
		location.save();
		return super.visit(node);
	}

	@Override
	public boolean visit(NumberLiteral node) {
		if ((!visitMethod && inMethod) || (visitMethod && !inMethod)) {
			return super.visit(node);
		}
		String key = rawType.getKey() + node.getToken();
		ITypeBinding type = node.resolveTypeBinding();
		Element elem = Element.findByKey(key);
		if (elem == null) {
			elem = new Element();
			elem.setElementText(node.toString());
			int kind = node.getNodeType();
			elem.setNodeKind(kind);
			elem.setNodeKindText(NodeType.getByType(kind).getText());
			elem.setElementKey(key);
			elem.setTypeKey(type.getKey());
			elem.setTypeQualified(type.getQualifiedName());
			elem.setTypeText(type.getName());
		}

		elem = elem.save();
		ElementLocation location;
		location = new ElementLocation();
		location.setElement(elem);
		location.setPakage(this.pakage);
		location.setType(this.type);
		location.setMethod(this.method);
		IResource res = rawType.getJavaElement().getResource();
		if (res != null) {
			location.setFile(res.getRawLocationURI().toString());
			location.setOffset(node.getStartPosition());
			location.setLength(node.getLength());
			location.setLineEnd(compilationUnit.getLineNumber(node
					.getStartPosition() + node.getLength()));
			location.setLineStart(compilationUnit.getLineNumber(node
					.getStartPosition()));
		}
		location.save();
		return super.visit(node);
	}

	@Override
	public boolean visit(StringLiteral node) {
		if ((!visitMethod && inMethod) || (visitMethod && !inMethod)) {
			return super.visit(node);
		}
		if (node.getLiteralValue() == null
				|| node.getLiteralValue().length() == 0) {
			return super.visit(node);
		}
		String key = rawType.getKey() + node.getEscapedValue();
		ITypeBinding type = node.resolveTypeBinding();
		Element elem = Element.findByKey(key);
		if (elem == null) {
			elem = new Element();
			elem.setElementText(node.toString());
			int kind = node.getNodeType();
			elem.setNodeKind(kind);
			elem.setNodeKindText(NodeType.getByType(kind).getText());
			elem.setElementKey(key);
			elem.setTypeKey(type.getKey());
			elem.setTypeQualified(type.getQualifiedName());
			elem.setTypeText(type.getName());
		}

		elem = elem.save();
		ElementLocation location;
		location = new ElementLocation();
		location.setElement(elem);
		location.setPakage(this.pakage);
		location.setType(this.type);
		location.setMethod(this.method);
		IResource res = rawType.getJavaElement().getResource();
		if (res != null) {
			location.setFile(res.getRawLocationURI().toString());
			location.setOffset(node.getStartPosition());
			location.setLength(node.getLength());
			location.setLineEnd(compilationUnit.getLineNumber(node
					.getStartPosition() + node.getLength()));
			location.setLineStart(compilationUnit.getLineNumber(node
					.getStartPosition()));
		}
		location.save();
		return super.visit(node);
	}
}
