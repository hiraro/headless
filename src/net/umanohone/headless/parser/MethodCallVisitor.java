package net.umanohone.headless.parser;

import java.util.ArrayList;
import java.util.List;

import net.umanohone.headless.models.Type;
import net.umanohone.headless.models.Method;
import net.umanohone.headless.models.MethodCallMap;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodCallVisitor extends ASTVisitor {
	public List<MethodCallMap> methodCalls = new ArrayList<MethodCallMap>();
	public CompilationUnit compilationUnit;

	@Override
	public boolean visit(MethodInvocation node) {
		MethodCallMap map = new MethodCallMap();
		map.setText(node.toString());

		IMethodBinding resolved = node.resolveMethodBinding();
		if (resolved != null && resolved.getKind() == IBinding.METHOD) {
			IMethodBinding method = node.resolveMethodBinding();
			IType type = (IType) method.getDeclaringClass().getJavaElement();
			Type clazz = Type.create(compilationUnit, type, true);
			Method callee = Method.create(compilationUnit, method,
					compilationUnit.findDeclaringNode(method.getKey()), clazz,
					true);
			map.setCallee(callee);
			methodCalls.add(map);
		}
		return super.visit(node);
	}
}