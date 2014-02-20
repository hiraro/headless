package net.umanohone.headless.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;

/**
 * メソッド-呼び出しメソッドのマッピング
 */
@Entity
public class MethodCallMap {

	@Id
	private Long mapId;
	@Column(columnDefinition = "LONGTEXT")
	private String text;
	@ManyToOne
	@JoinColumn(name = "caller_id")
	private Method caller;
	@ManyToOne
	@JoinColumn(name = "callee_id")
	private Method callee;
	@Version
	private Timestamp lastUpdate;

	public static MethodCallMap getById(Long mapId) {
		if (mapId == null) {
			return null;
		}
		return Ebean.find(MethodCallMap.class).where().eq("map_id", mapId)
				.findUnique();
	}

	@Transactional
	public MethodCallMap save() {
		Ebean.save(this);
		return this;
	}

	public Long getMapId() {
		return mapId;
	}

	public void setMapId(Long mapId) {
		this.mapId = mapId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Method getCaller() {
		return caller;
	}

	public void setCaller(Method caller) {
		this.caller = caller;
	}

	public Method getCallee() {
		return callee;
	}

	public void setCallee(Method callee) {
		this.callee = callee;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
