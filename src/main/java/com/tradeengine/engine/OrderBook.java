package com.tradeengine.engine;

import com.tradeengine.model.Asset;
import com.tradeengine.model.Order;
import com.tradeengine.model.enums.OrderSide;
import java.util.Comparator;
import java.util.PriorityQueue;

public class OrderBook {
    private final Asset asset;

    private final PriorityQueue<Order> buyOrders;

    private final PriorityQueue<Order> sellOrders;

    public OrderBook(Asset asset) {
        this.asset = asset;

        Comparator<Order> bidComparator = Comparator
                .comparing(Order::getPrice, (p1, p2) -> p2.amount().compareTo(p1.amount()))
                .thenComparing(Order::getCreatedAt);

        Comparator<Order> askComparator = Comparator
                .comparing(Order::getPrice, (p1, p2) -> p1.amount().compareTo(p2.amount()))
                .thenComparing(Order::getCreatedAt);

        this.buyOrders = new PriorityQueue<>(bidComparator);
        this.sellOrders = new PriorityQueue<>(askComparator);
    }

    public void addOrder(Order order) {
        if (!order.getAsset().getSymbol().equals(this.asset.getSymbol())) {
            throw new IllegalArgumentException("Cannot add order for " + order.getAsset().getSymbol()
                    + " to order book of " + this.asset.getSymbol());
        }

        order.accept();

        if (order.getSide() == OrderSide.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }

    public Order peekBestBuy() {
        return buyOrders.peek();
    }

    public Order peekBestSell() {
        return sellOrders.peek();
    }

    public Order pollBestBuy() {
        return buyOrders.poll();
    }

    public Order pollBestSell() {
        return sellOrders.poll();
    }

    public Asset getAsset() {
        return asset;
    }

    public int getBuyOrderCount() {
        return buyOrders.size();
    }

    public int getSellOrderCount() {
        return sellOrders.size();
    }

}
