# Pelican

Pelican is a tiny and fun realtime micro-auctionning experimental webapp. 
Select a product, start auctionning at $0.01, $0.02 or $0.05... There are no other higher bid option! But that's all the point.
In realtime you will see the countdown running out of time and other people putting bid on the product. Be the fastest to bid!

![](https://github.com/spreadthesource/pelican/raw/master/pelican.jpg)

## Getting Started

Requirements:

* Maven 3 or more recent
* [Hornet](https://github.com/nectify/hornet) realtime engine

Steps:

* Download the sources
* Go to the repository directory
* Start redis and hornet
* Run "mvn jetty:run"
* Open your browser to http://localhost:8080/pelican
* Log in with defaults account in two differents browsers , ( joe/password , howard/password)
* Bid from one browser on an item, see the bid updated live on the other

## More Informations & contacts

* Twitter: http://twitter.com/spreadthesource