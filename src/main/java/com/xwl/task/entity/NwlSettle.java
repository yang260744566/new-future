package com.xwl.task.entity;

import java.util.Date;

public class NwlSettle  {
	private Integer id;
	private String suid;
	private Double money;//余额
	private Integer status;//
	private Integer place;
	private Date crdt;//创建时间
	private Date updt;//修改时间
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSuid() {
		return suid;
	}
	public void setSuid(String suid) {
		this.suid = suid;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPlace() {
		return place;
	}
	public void setPlace(Integer place) {
		this.place = place;
	}
	public Date getCrdt() {
		return crdt;
	}
	public void setCrdt(Date crdt) {
		this.crdt = crdt;
	}
	public Date getUpdt() {
		return updt;
	}
	public void setUpdt(Date updt) {
		this.updt = updt;
	}
	public NwlSettle() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NwlSettle(Integer id, String suid, Double money, Integer status, Integer place, Date crdt, Date updt) {
		super();
		this.id = id;
		this.suid = suid;
		this.money = money;
		this.status = status;
		this.place = place;
		this.crdt = crdt;
		this.updt = updt;
	}
	

}