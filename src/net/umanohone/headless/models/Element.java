package net.umanohone.headless.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;

/**
 * 単語のデータ
 */
@Entity
public class Element {

	@Id
	private Long elementId;
	@Column(columnDefinition = "LONGTEXT")
	private String elementKey;
	@Column(columnDefinition = "LONGTEXT")
	private String elementText;
	@Column(columnDefinition = "LONGTEXT")
	private String elementOption;// シグネチャなど
	private int nodeKind;
	private String nodeKindText;
	@Column(columnDefinition = "LONGTEXT")
	private String typeKey;
	private String typeText;
	private String typeQualified;
	@Version
	private Timestamp lastUpdate;
	private boolean isComment=false;

	public static Element findInListByKey(List<Element> list, String key) {
		for (Element e : list) {
			if (e.getElementKey().equals(key)) {
				return e;
			}
		}
		return null;
	}

	public static Element findByKey(String key) {
		return Ebean.find(Element.class).where().eq("element_key", key)
				.findUnique();
	}

	public static List<Element> findAll() {
		return Ebean.find(Element.class).findList();
	}

	@Transactional
	public Element save() {
		if (this.getElementKey() == null) {
			return null;
		}

		Element found = findByKey(this.getElementKey());
		if (found != null) {
			return found;
		}
		Ebean.save(this);
		return this;
	}

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	public String getElementKey() {
		return elementKey;
	}

	public void setElementKey(String elementKey) {
		this.elementKey = elementKey;
	}

	public String getElementText() {
		return elementText;
	}

	public void setElementText(String elementText) {
		this.elementText = elementText;
	}

	public String getElementOption() {
		return elementOption;
	}

	public void setElementOption(String elementOption) {
		this.elementOption = elementOption;
	}

	public int getNodeKind() {
		return nodeKind;
	}

	public void setNodeKind(int nodeKind) {
		this.nodeKind = nodeKind;
	}

	public String getNodeKindText() {
		return nodeKindText;
	}

	public void setNodeKindText(String nodeKindText) {
		this.nodeKindText = nodeKindText;
	}

	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	public String getTypeText() {
		return typeText;
	}

	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}

	public String getTypeQualified() {
		return typeQualified;
	}

	public void setTypeQualified(String typeQualified) {
		this.typeQualified = typeQualified;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public boolean isComment() {
		return isComment;
	}

	public void setComment(boolean isComment) {
		this.isComment = isComment;
	}
}