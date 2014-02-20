package net.umanohone.headless.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import net.umanohone.headless.models.Element;
import net.umanohone.headless.models.ElementLocation;
import net.umanohone.headless.models.Method;
import net.umanohone.headless.models.Package;
import net.umanohone.headless.models.Type;
import net.umanohone.headless.parser.ElementVisitor.NodeType;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.TextElement;

public class CommentVisitor extends ASTVisitor {
	public static final int MIN_COMMENT_LENGTH = 3;
	public static final int MAX_COMMENT_LENGTH = 20;
	private IBinding[] methods;
	private Package pakage;
	private Type type;
	private ITypeBinding rawType;
	private StringBuffer src;
	private CompilationUnit compilationUnit;
	public List<ElementLocation> comments = new ArrayList<>();

	public CommentVisitor(CompilationUnit unit, ICompilationUnit iunit,
			IBinding[] methods, IPackageFragment pakage, ITypeBinding type) {
		super(true);
		this.compilationUnit = unit;
		this.methods = methods;
		this.pakage = Package.create(pakage, true);
		this.type = Type.create(unit, type, true);
		this.rawType = type;
		try {
			URI uri = iunit.getResource().getRawLocationURI();
			BufferedReader br = new BufferedReader(
					new FileReader(new File(uri)));
			src = new StringBuffer();
			while (br.ready()) {
				src.append((char) br.read());
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean visit(LineComment node) {
		String comment = getCommentText(node);
		IBinding method = findTargetMethod(node);
		visitSub(comment, node, method);
		return super.visit(node);
	}

	public boolean visit(BlockComment node) {
		String comment = getCommentText(node);
		IBinding method = findTargetMethod(node);
		visitSub(comment, node, method);
		return super.visit(node);
	}

	public boolean visit(Javadoc node) {
		String comment = getCommentText(node);
		IBinding method = findTargetMethod(node);
		visitSub(comment, node, method);
		return super.visit(node);
	}

	private void visitSub(String comment, ASTNode node, IBinding method) {
		if (comment != null && comment.length() > MIN_COMMENT_LENGTH) {
			Element e = new Element();
			e.setElementKey(rawType.getKey() + node.getStartPosition());
			e.setElementText(comment);
			e.setNodeKind(node.getNodeType());
			e.setNodeKindText(NodeType.getByType(node.getNodeType()).getText());
			e.setComment(true);
			e.save();
			ElementLocation el = new ElementLocation();
			el.setElement(e);
			el.setPakage(pakage);
			el.setType(type);

			el.setFile(rawType.getJavaElement().getResource()
					.getRawLocationURI().toString());
			el.setOffset(node.getStartPosition());
			el.setLength(node.getLength());

			if (method != null) {
				el.setMethod(Method.create(compilationUnit, method, type, true));
			}
			el.save();
		}
	}

	private String getCommentText(LineComment node) {
		int nodeStart = node.getStartPosition() + 2;
		int nodeEnd = nodeStart + node.getLength();
		return src.substring(nodeStart, nodeEnd).trim();
	}

	private String getCommentText(BlockComment node) {
		int nodeStart = node.getStartPosition() + 2;
		int nodeEnd = nodeStart + node.getLength() - 4;
		LineNumberReader lnr = new LineNumberReader(new StringReader(
				src.substring(nodeStart, nodeEnd)));
		StringBuffer sb = new StringBuffer();
		try {
			for (String temp = lnr.readLine(); temp != null; temp = lnr
					.readLine()) {
				sb.append(temp.replaceFirst("\\*", "").trim());
				sb.append(" ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString().trim();
	}

	private String getCommentText(Javadoc node) {
		int nodeStart = node.getStartPosition() + 3;
		int nodeEnd = nodeStart + node.getLength() - 5;
		LineNumberReader lnr = new LineNumberReader(new StringReader(
				src.substring(nodeStart, nodeEnd)));
		StringBuffer sb = new StringBuffer();
		try {
			for (String temp = lnr.readLine(); temp != null; temp = lnr
					.readLine()) {
				sb.append(temp.replaceFirst("\\*", "").trim());
				sb.append(" ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString().trim();
	}

	private IBinding findTargetMethod(ASTNode node) {
		int nodeStart = node.getStartPosition();
		int nodeEnd = nodeStart + node.getLength();
		int start;
		int end;
		IMethod method;
		try {
			for (IBinding m : methods) {
				if (m.getKind() != IBinding.METHOD) {
					continue;
				}
				method = (IMethod) m.getJavaElement();
				start = method.getSourceRange().getOffset();
				end = start + method.getSourceRange().getLength();
				if (start <= nodeStart && nodeEnd <= end) {
					return m;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
