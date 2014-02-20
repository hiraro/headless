package net.umanohone.headless;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import net.umanohone.headless.utils.Util;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ProjectWalker extends Observable {
	public static final String ON_PROJECT = "project";
	public static final String ON_PACKAGE = "package";
	public static final String ON_COMPILATION_UNIT = "compilation_unit";
	public static final String ON_TYPE = "type";
	public static final String ON_RESOLVE_METHODS = "resolve_methods";
	public static final String ON_METHOD = "method";

	private List<String> exclusion = null;
	private List<String> inclusion = null;
	private IJavaProject project;
	private IPackageFragment packageFragment;
	private CompilationUnit compilationUnit;
	private ICompilationUnit iCompilationUnit;
	private IType type;
	private IBinding[] typeBindings;
	private IBinding typeBinding;
	private IBinding[] methodBindings;
	private IBinding methodBinding;

	public List<String> getInclusion() {
		return inclusion;
	}

	public void setInclusion(List<String> inclusion) {
		this.inclusion = inclusion;
	}

	public List<String> getExclusion() {
		return exclusion;
	}

	public void setExclusion(List<String> exclusion) {
		this.exclusion = exclusion;
	}

	public ICompilationUnit getICompilationUnit() {
		return iCompilationUnit;
	}

	public IBinding[] getTypeBindings() {
		return typeBindings;
	}

	public ITypeBinding getTypeBinding() {
		if (typeBinding.getKind() == IBinding.TYPE) {
			return (ITypeBinding) typeBinding;
		}
		return null;
	}

	public IMethodBinding getMethodBinding() {
		if (methodBinding.getKind() == IBinding.METHOD) {
			return (IMethodBinding) methodBinding;
		}
		return null;
	}

	public IBinding[] getMethodBindings() {
		return methodBindings;
	}

	public IJavaProject getProject() {
		return project;
	}

	public IPackageFragment getPackageFragment() {
		return packageFragment;
	}

	public CompilationUnit getCompilationUnit() {
		return compilationUnit;
	}

	public IType getType() {
		return type;
	}

	public ProjectWalker(IJavaProject project) {
		this.project = project;
	}

	private boolean skip(String name) {
		boolean flag = true;
		for (String s : inclusion) {
			if (s.equals("*")) {
				flag = false;
				break;
			} else if (s.indexOf("*") != -1
					&& name.startsWith(s.substring(0, s.length() - 1))) {
				flag = false;
				break;
			} else if (name.startsWith(s)) {
				flag = false;
				break;
			}
		}
		for (String s : exclusion) {
			if (s.equals("*")) {
				flag = true;
				break;
			} else if (s.indexOf("*") != -1
					&& name.startsWith(s.substring(0, s.length() - 1))) {
				flag = true;
				break;
			} else if (name.startsWith(s)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public void execute() {
		try {
			setChanged();
			notifyObservers(ON_PROJECT);
			List<IPackageFragment> packages = new ArrayList<>();
			for (IPackageFragment pf : project.getPackageFragments()) {
				if (pf.getKind() == IPackageFragmentRoot.K_SOURCE) {
					packages.add(pf);
				}
			}
			int packIndex = 0;
			for (IPackageFragment pf : packages) {
				packIndex++;
				Application.logger.warning("Package : " + (packIndex)
						+ " / " + packages.size());
				if (skip(pf.getElementName())) {
					continue;
				}
				packageFragment = pf;
				setChanged();
				notifyObservers(ON_PACKAGE);
				int unitIndex = 0;
				for (ICompilationUnit unit : pf.getCompilationUnits()) {
					unitIndex++;
					Application.logger.warning("Unit : " + (unitIndex) + " / "
							+ pf.getCompilationUnits().length);
					compilationUnit = Util.parseAST(unit, project);
					this.iCompilationUnit = unit;
					setChanged();
					notifyObservers(ON_COMPILATION_UNIT);
					IType[] allTypes = unit.getAllTypes();
					if (allTypes != null && allTypes.length > 0) {
						typeBindings = Util.createASTParser(unit, project)
								.createBindings(allTypes, null);
					}
					for (int i = 0; i < allTypes.length; i++) {
						this.type = allTypes[i];
						if (allTypes != null && allTypes.length > 0) {
							this.typeBinding = typeBindings[i];
						}
						Application.logger
								.warning(type.getFullyQualifiedName());
						setChanged();
						notifyObservers(ON_TYPE);
						IMethod[] methods = type.getMethods();
						methodBindings = Util.createASTParser(unit, project)
								.createBindings(methods, null);
						setChanged();
						notifyObservers(ON_RESOLVE_METHODS);
						for (int j = 0; j < methodBindings.length; j++) {
							methodBinding = methodBindings[j];
							setChanged();
							notifyObservers(ON_METHOD);
						}
					}
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}
}
