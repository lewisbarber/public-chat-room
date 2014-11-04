<html>
  <head>
    <title>Public Chat Room</title>
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Ropa+Sans:400,400italic' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="css/main.css" />
    <script type="text/javascript" src="js/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="js/sockjs-0.3.4.js"></script>
    <script type="text/javascript" src="js/stomp.js"></script>
    <script type="text/javascript" src="js/main.js"></script>
    <link rel="icon" type="image/png" href="img/favicon.png" />
  </head>
  <body>
	  <div class="wrapper">
	  	<div class="inner">
		  	<h1>Public Chat Room</h1>
		  	<p>Welcome to my public chat room application. You can join the conversation by entering your name below:</p>
		  	<div class="enter-name">
		  		<form autocomplete="off" id="enter-name-form" method="POST">
		  			<input autocomplete="off" type="text" id="username" placeholder="Enter your name:" />
		  			<button id="join-button">Join Conversation</button> 
		  		</form>
		  		<div id="error-msg" class="error-msg"></div>
		  	</div>
			<div class="main-holder">
				<div class="messages-holder">
				    <div class="messages disabled"></div>
				    <div class="compose-message">
					    <form id="send-message-form">
					    	<textarea class="disabled" disabled="disabled" id="new-message" placeholder="Enter your message here:"></textarea>
					    	<button class="send-message disabled" disabled="disabled">Send</button>
					    </form>
				    </div>
				</div>
			    <div class="connected-users">
			    	<div class="title">Connected Users:</div>
			    	<div id="join-convo-msg">Join conversation to view users</div>
			    </div>
			    <div class="clear"></div>
			</div>
		 </div>
	  </div>
  </body>
</html>