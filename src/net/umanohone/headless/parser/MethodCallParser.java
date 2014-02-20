package net.umanohone.headless.parser;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import net.umanohone.headless.Application;
import net.umanohone.headless.ProjectWalker;
import net.umanohone.headless.models.Type;
import net.umanohone.headless.models.Method;
import net.umanohone.headless.models.MethodCallMap;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodCallParser implements Observer {

	private void execute(CompilationUnit compilationUnit,
			IMethodBinding callerBinding) {
		if (callerBinding == null || compilationUnit == null) {
			return;
		}

		MethodCallVisitor visitor = new MethodCallVisitor();
		visitor.compilationUnit = compilationUnit;
		ASTNode node = compilationUnit
				.findDeclaringNode(callerBinding.getKey());
		if (node == null) {
			return;
		}
		Application.logger.info("###MethodCallParser###"
				+ ((MethodDeclaration) node).getName());
		node.accept(visitor);

		if (callerBinding.getKind() == IBinding.METHOD) {
			IType type = (IType) callerBinding.getDeclaringClass()
					.getJavaElement();
			Type clazz = Type.create(compilationUnit, type, true);
			List<MethodCallMap> callMaps = visitor.methodCalls;
			Method caller = Method.create(compilationUnit, callerBinding,
					compilationUnit.findDeclaringNode(callerBinding.getKey()),
					clazz, true);
			for (MethodCallMap map : callMaps) {
				map.setCaller(caller);
				map.save();
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		ProjectWalker pt = ((ProjectWalker) o);
		if (arg.toString().equals(ProjectWalker.ON_METHOD)) {
			execute(pt.getCompilationUnit(), pt.getMethodBinding());
		}
	}

}
