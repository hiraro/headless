package net.umanohone.headless.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;

/**
 * クラス
 */
@Entity
public class Type {


	@Id
	private Long typeId;
	@Column(columnDefinition = "LONGTEXT")
	private String typeKey;
	private String name;
	private String qualifiedName;
	@ManyToOne
	@JoinColumn(name = "pakage_id")
	private Package pakage;
	@Column(columnDefinition = "LONGTEXT")
	private String file;
	private int offset;
	private int length;
	private int lineStart;
	private int lineEnd;
	@Version
	private Timestamp lastUpdate;


	public static Type create(CompilationUnit cu, ITypeBinding type,
			boolean save) {
		Type instance = findOrCreate(type.getKey());
		instance.setTypeKey(type.getKey());
		instance.setName(type.getName());
		instance.setQualifiedName(type.getQualifiedName());
		instance.setPakage(Package.create(((IPackageFragment) type.getPackage()
				.getJavaElement()), true));

		ASTNode node = cu.findDeclaringNode(type.getKey());
		if (node != null) {
			instance.setOffset(node.getStartPosition());
			instance.setLength(node.getLength());
			IJavaElement elem = type.getJavaElement();
			instance.setFile(elem.getResource().getRawLocationURI().toString());
			instance.setLineStart(cu.getLineNumber(node.getStartPosition()));
			instance.setLineEnd(cu.getLineNumber(node.getStartPosition()+node.getLength()));
		}

		ITypeBinding superClazz = type.getSuperclass();
		if (superClazz != null) {
			TypeHierarchie.create(superClazz.getKey(), type.getKey());
		}
		ITypeBinding[] superInterfaces = type.getInterfaces();
		if (superInterfaces != null && superInterfaces.length > 0) {
			for (ITypeBinding superInterface : superInterfaces) {
				TypeHierarchie.create(superInterface.getKey(), type.getKey());
			}
		}
		if (save) {
			return instance.save();
		}
		return instance;
	}

	public static Type create(CompilationUnit cu, IType type, boolean save) {
		Type instance = findOrCreate(type.getKey());
		instance.setTypeKey(type.getKey());
		instance.setName(type.getElementName());
		instance.setQualifiedName(type.getFullyQualifiedName());
		instance.setPakage(Package.create(type.getPackageFragment(), true));

		if (save) {
			return instance.save();
		}
		return instance;
	}

	public static Type findOrCreate(String key) {
		Type ret = getByKey(key);
		if (ret == null) {
			ret = new Type();
		}
		return ret;
	}

	public static Type getByKey(String key) {
		if (key == null) {
			return null;
		}
		return Ebean.find(Type.class).where().eq("type_key", key).findUnique();
	}

	@Transactional
	public Type save() {
		if (getTypeKey() == null) {
			return null;
		}
		Type found = getByKey(this.getTypeKey());
		if (found != null) {
			Ebean.update(this);
			return found;
		}
		Ebean.save(this);
		return this;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public Package getPakage() {
		return pakage;
	}

	public void setPakage(Package pakage) {
		this.pakage = pakage;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getLineStart() {
		return lineStart;
	}

	public void setLineStart(int lineStart) {
		this.lineStart = lineStart;
	}

	public int getLineEnd() {
		return lineEnd;
	}

	public void setLineEnd(int lineEnd) {
		this.lineEnd = lineEnd;
	}
}
