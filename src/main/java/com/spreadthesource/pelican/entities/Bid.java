	package com.spreadthesource.pelican.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.apache.tapestry5.beaneditor.NonVisual;

import com.spreadthesource.pelican.entities.Item;
import com.spreadthesource.pelican.entities.User;

@Entity
@NamedQuery(name="com.spreadthesource.pelican.entities.Bid.findMaxBid",
			query="SELECT max(b.price) FROM Bid b WHERE b.item=:item")
public class Bid {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonVisual
	private long id;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Item item;
	
	private Date date;
	
	private long price;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}
}
