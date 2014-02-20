package net.umanohone.headless.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;

@Entity
public class TypeHierarchie {


	@Id
	private Long mapId;
	@Column(columnDefinition = "LONGTEXT")
	private String parentKey;
	@Column(columnDefinition = "LONGTEXT")
	private String childKey;
	@Version
	private Timestamp lastUpdate;


	public static TypeHierarchie create(String parent,String child) {
		if(child==null||parent==null||parent.equals("Ljava/lang/Object;")){
			return null;
		}
		TypeHierarchie inst=new TypeHierarchie();
		inst.setParentKey(parent);
		inst.setChildKey(child);
		return inst.save();
	}

	@Transactional
	public TypeHierarchie save() {
		Ebean.save(this);
		return this;
	}

	public String getParentKey() {
		return parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	public String getChildKey() {
		return childKey;
	}

	public void setChildKey(String childKey) {
		this.childKey = childKey;
	}

	public Long getMapId() {
		return mapId;
	}

	public void setMapId(Long mapId) {
		this.mapId = mapId;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
