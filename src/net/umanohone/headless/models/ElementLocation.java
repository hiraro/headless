package net.umanohone.headless.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;

/**
 * メソッド-単語のマッピング
 */
@Entity
public class ElementLocation {

	@Id
	private Long mapId;
	@ManyToOne
	@JoinColumn(name = "pakage_id")
	private Package pakage;
	@ManyToOne
	@JoinColumn(name = "type_id")
	private Type type;
	@ManyToOne
	@JoinColumn(name = "method_id")
	private Method method;
	@ManyToOne
	@JoinColumn(name = "element_id")
	private Element element;
	@Column(columnDefinition = "LONGTEXT")
	private String file;
	private int offset;
	private int length;
	private int lineStart;
	private int lineEnd;
	@Version
	private Timestamp lastUpdate;

	public static ElementLocation getById(Long id) {
		return Ebean.find(ElementLocation.class).where().idEq(id).findUnique();
	}

	public static ElementLocation findByElementKey(String key) {
		return Ebean.find(ElementLocation.class).fetch("element").where()
				.eq("element_key", key).findUnique();
	}

	public static ElementLocation findOrCreate(String key) {
		ElementLocation found = findByElementKey(key);
		if (found != null) {
			return found;
		} else {
			return new ElementLocation();
		}
	}

	@Transactional
	public void save() {
		Ebean.save(this);
	}

	public Long getMapId() {
		return mapId;
	}

	public void setMapId(Long mapId) {
		this.mapId = mapId;
	}

	public Package getPakage() {
		return pakage;
	}

	public void setPakage(Package pakage) {
		this.pakage = pakage;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
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


	@Override
	public String toString() {
		return "ElementLocation [" + (mapId != null ? "mapId=" + mapId : "")
				+ "]";
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