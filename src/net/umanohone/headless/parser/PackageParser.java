package net.umanohone.headless.parser;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jdt.core.IPackageFragment;

import net.umanohone.headless.Application;
import net.umanohone.headless.ProjectWalker;
import net.umanohone.headless.models.Package;

public class PackageParser implements Observer {
	public void execute(IPackageFragment p) {
		if (p == null) {
			return;
		}
		Application.logger.info("###PackageParser###" + p.getElementName());

		Package.create(p, true, true);
	}

	@Override
	public void update(Observable o, Object arg) {
		ProjectWalker pt = ((ProjectWalker) o);
		if (arg.toString().equals(ProjectWalker.ON_PACKAGE)) {
			execute(pt.getPackageFragment());
		}
	}
}
