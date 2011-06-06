package com.spreadthesource.pelican.entities;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.NonVisual;

@Entity
public class Item {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonVisual
	private long id;
	
	private String name;
	
	private String description;
	
	private long price;
	
	@OneToMany(mappedBy="item")
	@Property
	private Collection<Bid> bids;
	
	public Item() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	
	@Override
	public String toString() {

		return this.name;
	}
}
