var stompClient = null;
var username = null;
var windowFocus = false;
//var appContext = "";
var appContext = "/chat";

var guid = (function() {
  function section() {
    return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
  }
  return function() {
	return section() + section() + '-' + section() + '-' + section() + '-' + section() + '-' + section() + section() + section();
  };
})();

String.prototype.replaceAt = function(index, character) {
    return this.substr(0, index) + character + this.substr(index+character.length);
};

var connect = function() {
	
	var login = guid();
	
	var socket = new SockJS(appContext + '/socket/init');
    
    stompClient = Stomp.over(socket);
	
    stompClient.connect(login, username, function(frame) {
    	
    	var msg = {
    		'username':username
    	};
    	
    	stompClient.subscribe('/message/new-seen-by', function(message) {
    		
    		msgObj = JSON.parse(message.body);
    		
    		if(msgObj.seenBy != null) {
    		
	    		var newSeenBy = msgObj.seenBy.replace(username + ",", "").replace(", " + username, "");
	    		var lastCommaIndex = newSeenBy.lastIndexOf(",");
	    		var oneName = false;
	    		if(lastCommaIndex > -1) {
	    			var finalSeenBy = newSeenBy.substring(0, lastCommaIndex) + " &amp;" + newSeenBy.substring(lastCommaIndex + 1, newSeenBy.length);
	    		} else {
	    			oneName = true;
	    			var finalSeenBy = newSeenBy;
	    		}
	    		
	    		$('#seen-by').remove();
		    	$('.messages').append('<div class="seen-by" id="seen-by">Seen by ' + finalSeenBy + '</div>');
		    	
		    	if(oneName && finalSeenBy.indexOf(username) > -1) {
		    		$('#seen-by').remove();	
		    	}

	    		$(".messages").scrollTop($(".messages")[0].scrollHeight); 
	    		
    		}
    		
    	});
    	
    	stompClient.subscribe('/user/' + login + '/messages/receive', function(message) {
    		
    		msgObj = JSON.parse(message.body);
    		
    		console.log(msgObj);
    		
    		for(var i=0; i<msgObj.length; i++) {
    			$('.messages').append('<div class="message"><span class="msg-username">' + msgObj[i].username + ':</span> ' + msgObj[i].message + '</div>')
    		}
    		
    		$(".messages").scrollTop($(".messages")[0].scrollHeight); 
    		
    		stompClient.send("/message/update-seen-by", {}, JSON.stringify({ 'username': username }));
    		
    	});
    	
        stompClient.subscribe('/message/receive', function(message) {
        	
            msgObj = JSON.parse(message.body);
            
        	$('.messages').append('<div class="message"><span class="msg-username">' + msgObj.username + ':</span> ' + msgObj.message + '</div>');
        	if(msgObj.username == username) {
	        	$('#new-message').val(null);
	        }
        	
        	if(windowFocus) {
        		stompClient.send("/message/update-seen-by", {}, JSON.stringify({ 'username': username }));
        	}
        	
        	$(".messages").scrollTop($(".messages")[0].scrollHeight);
            
        });
    	
        stompClient.subscribe('/client-sessions/receive', function(message) {
        	
            msgObj = JSON.parse(message.body);
            
            if(msgObj.length > 0) {
            	$('.user').remove();
            	$('#join-convo-msg').remove();
            }
            
            for(var i = 0; i<msgObj.length; i++) {
            	$('.connected-users').append('<div class="user"><i class="fa fa-circle-o online"></i> ' + msgObj[i].username + '</div>');
            }
            
        });
        
    	stompClient.send("/messages/fetch", {}, JSON.stringify(msg));
    	stompClient.send("/client-sessions/fetch", {}, JSON.stringify(msg));
        
    });
	
};

var sendMessage = function(message) {
	
	disableMessaging();
	
	var msg = {
		'message':message,
		'username':username
	};
	
	stompClient.send("/message/send", {}, JSON.stringify(msg));
	
	$('#seen-by').remove();
	
	enableMessaging();
	
};

function getCaret(el) {
  if (el.selectionStart) {
     return el.selectionStart;
  } else if (document.selection) {
     el.focus();

   var r = document.selection.createRange();
   if (r == null) {
    return 0;
   }

    var re = el.createTextRange(),
    rc = re.duplicate();
    re.moveToBookmark(r.getBookmark());
    rc.setEndPoint('EndToStart', re);

    return rc.text.length;
  }  
  return 0;
}

var enableUsername = function() {
	
	$('#username').removeAttr("disabled");
	$('#username').removeClass("disabled");
	$('#join-button').removeClass("disabled");
	$('#join-button').removeAttr("disabled");
	
};

var disableUsername = function() {

	$('#username').attr("disabled", "disabled");
	$('#username').addClass("disabled");
	$('#join-button').addClass("disabled");
	$('#join-button').attr("disabled", "disabled");
	
};

var enableMessaging = function() {
	
	$('.messages').removeClass("disabled");
	$('.send-message').removeClass("disabled");
	$('.send-message').removeAttr("disabled");
	$('#new-message').removeClass("disabled");
	$('#new-message').removeAttr("disabled");
	
};

var disableMessaging = function() {
	
	$('#new-message').addClass('disabled');
	$('#new-message').attr('disabled', "disabled");
	$('.send-message').addClass('disabled');
	$('.send-message').attr('disabled', "disabled");
	
};

$(document).ready(function(){
	
	$('#enter-name-form').submit(function(event){
		
		event.preventDefault();
		
		username = $('#username').val();

		if(username.trim() == "") {
			
			$('#error-msg').html("You must enter a username to join this conversation.");
			$('#error-msg').show();
			
			return;
			
		}
		
		$.ajax({
			type: "POST",
			url: "client-session/check-username",
			data: { 
				username: username 
			}
		}).done(function(response) {
			
			if(response.usernameInUse) {
				
				$('#error-msg').html("The username you entered is already in use, please enter a unique username and try again.");
				$('#error-msg').show();
				
				enableUsername();
				
			} else {
				
				$('#error-msg').html(null);
				$('#error-msg').hide();

				disableUsername();
				enableMessaging();
				connect();
				
			}
			
		}).fail(function(error) {
			
			console.log(error.status);
			console.log(error.statusText);
			console.log(error.responseText);
			
		});
		
	});
	
	$('#new-message').keydown(function(event) {
		
		if (event.keyCode == 13 && !event.shiftKey) {
		
			var msg = $(this).val();
			
			sendMessage(msg);
			
			event.preventDefault();
			
		}
		
	});
	
	$('#send-message-form').submit(function(event){
		
		var msg = $('#new-message').val();
		
		sendMessage(msg);
		
		event.preventDefault();
		
	});
	
	$(".messages").scrollTop($(".messages")[0].scrollHeight);

	$(window).focus(function() {
		windowFocus = true;
		if(stompClient != null) {
			stompClient.send("/message/update-seen-by", {}, JSON.stringify({ 'username': username }));
		}
	}).blur(function() {
		windowFocus = false;
	});
	
});