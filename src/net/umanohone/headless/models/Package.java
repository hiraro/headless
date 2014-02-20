package net.umanohone.headless.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;

/**
 * パッケージ
 */
@Entity
public class Package {
	public static final String DEFAULT_PACKAGE_NAME = "(Default Package)";

	@Id
	private Long pakageId;
	@Column(columnDefinition = "LONGTEXT")
	private String pakageName;
	private Integer kind;
	private Boolean parsed = false;
	@Version
	private Timestamp lastUpdate;

	public static Package create(IPackageFragment p, boolean save) {
		if (p == null) {
			return null;
		}
		String pakageName;
		if (p.getElementName().equals(IPackageFragment.DEFAULT_PACKAGE_NAME)) {
			pakageName = DEFAULT_PACKAGE_NAME;
		} else {
			pakageName = p.getElementName();
		}
		Package instance = findOrCreate(pakageName);
		instance.setPakageName(pakageName);
		try {
			IPackageFragmentRoot root = (IPackageFragmentRoot) p
					.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
			instance.setKind(root.getKind());
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		if (save) {
			return instance.save();
		}
		return instance;
	}

	public static Package create(IPackageFragment p, boolean save,
			boolean parsed) {
		Package ret = create(p, false);
		ret.setParsed(parsed);
		ret.save();
		return ret;
	}

	public static Package findOrCreate(String name) {
		Package ret = getByName(name);
		if (ret == null) {
			ret = new Package();
		}
		return ret;
	}

	public static Package getByName(String name) {
		if (name.equals(IPackageFragment.DEFAULT_PACKAGE_NAME)) {
			name = DEFAULT_PACKAGE_NAME;
		}
		return Ebean.find(Package.class).where().eq("pakage_name", name)
				.findUnique();
	}

	@Transactional
	public Package save() {
		if (getPakageName() == null) {
			return null;
		}
		Package found = getByName(getPakageName());
		if (found != null) {
			Ebean.update(this);
			return found;
		}
		Ebean.save(this);
		return this;
	}

	public Long getPakageId() {
		return pakageId;
	}

	public void setPakageId(Long pakageId) {
		this.pakageId = pakageId;
	}

	public String getPakageName() {
		return pakageName;
	}

	public void setPakageName(String pakageName) {
		this.pakageName = pakageName;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Boolean getParsed() {
		return parsed;
	}

	public void setParsed(Boolean parsed) {
		this.parsed = parsed;
	}

}
