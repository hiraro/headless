package net.umanohone.headless.utils;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Util {


	public static ASTParser createASTParser(ICompilationUnit src,
			IJavaProject project) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(src);
		parser.setUnitName(src.getPath().toString());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setProject(project);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setResolveBindings(true);
		return parser;
	}

	public static CompilationUnit parseAST(ICompilationUnit src,
			IJavaProject project) {
		return (CompilationUnit) createASTParser(src, project).createAST(
				new NullProgressMonitor());
	}

	public static final List<String> exclusionList = Arrays.asList("java.util",
			"java.lang");

	public static boolean matchExclusionPattern(String q) {
		for (String s : exclusionList) {
			if (q.startsWith(s)) {
				return true;
			}
		}
		return false;
	}
}
