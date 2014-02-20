package net.umanohone.headless.parser;

import java.util.Observable;
import java.util.Observer;

import net.umanohone.headless.Application;
import net.umanohone.headless.ProjectWalker;
import net.umanohone.headless.models.Type;
import net.umanohone.headless.models.Element;
import net.umanohone.headless.models.Method;
import net.umanohone.headless.models.ElementLocation;
import net.umanohone.headless.models.Package;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ElementParser implements Observer {

	public void execute(CompilationUnit compilationUnit,
			IMethodBinding methodBinding, ITypeBinding typeBind) {
		if (methodBinding == null || methodBinding.getKind() != IBinding.METHOD) {
			return;
		}
		ASTNode node = compilationUnit
				.findDeclaringNode(methodBinding.getKey());
		if (node == null) {
			return;
		}
		Application.logger.info("###ElementParser###" + methodBinding.getName());

		IType type = (IType) methodBinding.getDeclaringClass().getJavaElement();
		Package pakage = Package.create(type.getPackageFragment(), true);
		Type t = Type.create(compilationUnit, type, true);
		Method method = Method.create(compilationUnit, methodBinding,
				compilationUnit.findDeclaringNode(methodBinding.getKey()), t,
				true);

		ElementVisitor visitor = new ElementVisitor(compilationUnit, pakage, t,
				method, typeBind);
		visitor.setVisitMethod(true);
		node.accept(visitor);
	}

	@Override
	public void update(Observable o, Object arg) {
		ProjectWalker pt = ((ProjectWalker) o);
		if (arg.toString().equals(ProjectWalker.ON_METHOD)) {
			execute(pt.getCompilationUnit(), pt.getMethodBinding(),
					pt.getTypeBinding());
		}
	}

}
