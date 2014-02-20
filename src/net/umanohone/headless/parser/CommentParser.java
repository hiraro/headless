package net.umanohone.headless.parser;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import net.umanohone.headless.ProjectWalker;

public class CommentParser implements Observer {

	@SuppressWarnings("unchecked")
	public void execute(ICompilationUnit iunit, CompilationUnit unit,
			IPackageFragment pakage, ITypeBinding type, IBinding[] methodBinds) {
		List<Comment> comments = unit.getCommentList();
		CommentVisitor visitor = new CommentVisitor(unit, iunit, methodBinds,pakage,type);
		for (Comment c : comments) {
			c.accept(visitor);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		ProjectWalker pt = ((ProjectWalker) o);
		if (arg.toString().equals(ProjectWalker.ON_RESOLVE_METHODS)) {
			execute(pt.getICompilationUnit(), pt.getCompilationUnit(),
					pt.getPackageFragment(), pt.getTypeBinding(),
					pt.getMethodBindings());
		}

	}

}
