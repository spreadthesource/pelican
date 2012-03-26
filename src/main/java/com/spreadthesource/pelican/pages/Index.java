package com.spreadthesource.pelican.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import com.nectify.hornet.Hornet;
import com.spreadthesource.pelican.entities.Bid;
import com.spreadthesource.pelican.entities.Item;
import com.spreadthesource.pelican.entities.User;

@Import(stylesheet = {}, library =
{ "context:js/pelican.js" })
public class Index
{
    @SessionState
    @Property
    private User loggedUser;

    @Property
    private boolean loggedUserExists;

    @Inject
    private Logger logger;

    /**
     * Hornet connection token
     */
    @Property
    private String token;

    @Property
    private Item currentItem;

    @Property
    private String[] channels;

    // @Property
    private List<Item> listOfItems;

    @Property
    private Item item;

    @Property
    private long bidValue;

    @Component
    private Zone itemZone;

    @Component
    private Zone list;

    @Inject
    private Session session;

    @Inject
    private Hornet hornet;

    @Inject
    private JavaScriptSupport support;

    @SuppressWarnings("unchecked")
    @OnEvent(EventConstants.ACTIVATE)
    public Object initPage()
    {

        if (!loggedUserExists) return Login.class;

        List<String> channelsList = new ArrayList<String>();

        /*
         * In this demo, we create a channel for each item and a global one.
         */
        channelsList.add("global");
        for (Item item : (List<Item>) session.createCriteria(Item.class).list())
        {
            channelsList.add("bids-" + item.getId());
        }

        channels = channelsList.toArray(new String[0]);

        return null;
    }

    void afterRender(MarkupWriter writer)
    {
        JSONArray channelsAsJson = new JSONArray();
        /*
         * the goal here is to retrieve each item in order to subscribe to their channels
         */
        for (Item item : (List<Item>) session.createCriteria(Item.class).list())
        {
            channelsAsJson.put("bids-" + item.getId());
        }

        support.addScript(
                "var channels = %s; T5.define('hornetChannels', channels);",
                channelsAsJson);
    }

    @SuppressWarnings("unchecked")
    public List<Item> getlistOfItems()
    {
        return session.createCriteria(Item.class).addOrder(Order.asc("id")).list();
    }

    public long getMaxPrice()
    {
        return getMaxPrice(currentItem);
    }

    public long getMaxPrice(Item item)
    {

        Query query = (Query) session
                .getNamedQuery("com.spreadthesource.pelican.entities.Bid.findMaxBid");

        query.setParameter("item", item);

        long price = 0;

        try
        {
            price = (Long) query.uniqueResult();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            price = item.getPrice();
        }

        return price;
    }

    @OnEvent(value = EventConstants.ACTION, component = "ajax")
    public Object setItemFromUrl(long id, String token)
    {

        if (!loggedUserExists) return Login.class;

        item = (Item) session.createCriteria(Item.class).add(Restrictions.eq("id", id))
                .uniqueResult();
        bidValue = getMaxPrice(item);

        this.token = token;
        return itemZone.getBody();
    }

    @CommitAfter
    @OnEvent(value = "add")
    public Object addBid(int bidAmmnt, long itemId, String token)
    {
        item = (Item) session.createCriteria(Item.class).add(Restrictions.eq("id", itemId))
                .uniqueResult();
        long price = getMaxPrice(item);

        this.token = token;

        Bid bid = new Bid();
        bid.setDate(new Date());
        bid.setItem(item);
        bid.setPrice(price + bidAmmnt);
        bid.setUser(loggedUser);

        session.persist(bid);

        System.out.println("token is: " + token);

        JSONObject bidEvent = new JSONObject();
        bidEvent.put("itemId", item.getId());
        bidEvent.put("newPrice", bid.getPrice());

        hornet.publish("bids-" + itemId, "new_bid_on_item", bidEvent, "except", token);

        JSONObject globalbidEvent = new JSONObject();
        hornet.publish("global", "new_bid", globalbidEvent, "except", token);

        return new MultiZoneUpdate("itemZone", itemZone).add("list", list);
    }

}