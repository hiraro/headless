package net.umanohone.headless.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;

/**
 * メソッド
 */
@Entity
public class Method {

	@Id
	private Long methodId;
	@Column(columnDefinition = "LONGTEXT")
	private String methodKey;
	private String name;
	@Column(columnDefinition = "LONGTEXT")
	private String signature;
	@Column(columnDefinition = "LONGTEXT")
	private String parameterText;
	@Column(columnDefinition = "LONGTEXT")
	private String returnText;
	@ManyToOne
	@JoinColumn(name = "type_id")
	private Type type;
	@Column(columnDefinition = "LONGTEXT")
	private String file;
	private int offset;
	private int length;
	private int lineStart;
	private int lineEnd;
	@Version
	private Timestamp lastUpdate;


	private static Method create(CompilationUnit cu, IBinding method,
			ASTNode node) {
		Method instance;
		Method found = getByKey(method.getKey());
		if (found != null) {
			if (found.getReturnText() != null && found.getParameterText() != null) {
				return found;
			}
			instance = found;
		} else {
			instance = new Method();
		}
		instance.setMethodKey(method.getKey());
		instance.setName(method.getName());
		instance.setSignature(method.toString());
		if (node == null) {
			node = cu.findDeclaringNode(method.getKey());
		}
		if (node != null) {
			if(node.getNodeType()==ASTNode.METHOD_DECLARATION){
				MethodDeclaration dec = (MethodDeclaration) node;
				instance.setReturnText(dec.getReturnType2() == null ? "void" : dec
						.getReturnType2().toString());
				instance.setParameterText(dec.parameters().toString()
						.replaceAll("\\[", "").replaceAll("\\]", ""));
			}

			IJavaElement elem = method.getJavaElement();
			instance.setFile(elem.getResource().getRawLocationURI().toString());
			instance.setOffset(node.getStartPosition());
			instance.setLength(node.getLength());
			instance.setLineStart(cu.getLineNumber(node.getStartPosition()));
			instance.setLineEnd(cu.getLineNumber(node.getStartPosition()+node.getLength()));
		}

		return instance;
	}

	public static Method create(CompilationUnit cu, IBinding method,
			ASTNode node, Type type, boolean save) {
		Method instance = create(cu, method, node);
		instance.setType(type);
		if (save) {
			return instance.save();
		}
		return instance;
	}

	public static Method create(CompilationUnit cu, IBinding method, Type type,
			boolean save) {
		return create(cu, method, null, type, save);
	}

	public static Method getByKey(String key) {
		if (key == null) {
			return null;
		}
		return Ebean.find(Method.class).where().eq("method_key", key)
				.findUnique();
	}

	@Transactional
	public Method save() {
		if (getMethodKey() == null) {
			return null;
		}
		Method found = getByKey(getMethodKey());
		if (found != null) {
			if (getReturnText() != null || getParameterText() != null) {
				Ebean.update(this);
				return this;
			}
			return found;
		}
		Ebean.save(this);
		return this;
	}

	public Long getMethodId() {
		return methodId;
	}

	public void setMethodId(Long methodId) {
		this.methodId = methodId;
	}

	public String getMethodKey() {
		return methodKey;
	}

	public void setMethodKey(String methodKey) {
		this.methodKey = methodKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getParameterText() {
		return parameterText;
	}

	public void setParameterText(String parameterText) {
		this.parameterText = parameterText;
	}

	public String getReturnText() {
		return returnText;
	}

	public void setReturnText(String returnText) {
		this.returnText = returnText;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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
