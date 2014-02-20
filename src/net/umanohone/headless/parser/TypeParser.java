package net.umanohone.headless.parser;

import java.util.Observable;
import java.util.Observer;

import net.umanohone.headless.Application;
import net.umanohone.headless.ProjectWalker;
import net.umanohone.headless.models.Element;
import net.umanohone.headless.models.ElementLocation;
import net.umanohone.headless.models.Type;
import net.umanohone.headless.models.Method;
import net.umanohone.headless.models.Package;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class TypeParser implements Observer {
	public CompilationUnit compilationUnit;

	public void execute(CompilationUnit compilationUnit, IType type,
			ITypeBinding typeBind, IBinding[] methods) {
		if (type == null) {
			return;
		}
		ASTNode node = compilationUnit.findDeclaringNode(type.getKey());
		if (node == null) {
			return;
		}
		Application.logger.info("###TypeParser###" + type.getElementName());

		Package pakage = Package.create(type.getPackageFragment(), true);
		Type t = Type.create(compilationUnit, typeBind, true);

		// Parsing Method
		for (IBinding m : methods) {
			if (m.getKind() == IBinding.METHOD) {
				Method.create(compilationUnit, (IMethodBinding) m,
						compilationUnit.findDeclaringNode(m.getKey()), t, true);
			}
		}

		// Parsing Element and Location mapping
		ElementVisitor visitor = new ElementVisitor(compilationUnit, pakage, t,
				null, typeBind);
		visitor.setVisitMethod(false);
		ElementLocation loc;
		node.accept(visitor);
	}

	@Override
	public void update(Observable o, Object arg) {
		ProjectWalker pt = ((ProjectWalker) o);
		if (arg.toString().equals(ProjectWalker.ON_RESOLVE_METHODS)) {
			execute(pt.getCompilationUnit(), pt.getType(), pt.getTypeBinding(),
					pt.getMethodBindings());
		}
	}
}
