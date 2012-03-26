
(function($) {

$(window).load(function() {
  var hornet = T5.hornet
    , itemsChannels = T5.hornetChannels;
  
  $(hornet).log("hornet");
  $(itemsChannels).log("hornet items channels");
  
	hornet.on(itemsChannels, "new_bid_on_item", function(params) {
		var itemId = params["itemId"]
		  , newPrice = params["newPrice"];
		 
	  $(params).log(" new bind on item : " + itemId);
		
		
		$('#item-' + itemId).find(".price span").text(newPrice);
	});
	
  hornet.on('global', 'new_bid', function( params ) {
    $(params).log("global channel, new bid");
  });
  
});

})(jQuery);
