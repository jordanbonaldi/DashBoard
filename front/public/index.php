<html>
<head>
    <meta charset="UTF-8">
    <title>DashBoard</title>
    <link rel="stylesheet" href="./assets/hcc.css">
    <link rel="stylesheet" type="text/css" href="semantic/dist/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="semantic/range.css">
</head>

<body>
<!-- VUE instance-->

<render>  </render>

<!-- SCRIPTS -->


<script
        src="https://code.jquery.com/jquery-3.1.1.min.js"
        integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
        crossorigin="anonymous">
</script>

<script src="js/main.js"></script>

<script src="semantic/dist/semantic.min.js"></script>
<script src="semantic/range.js"></script>

<script src="./assets/hcc.js"></script>


<script>

	function statusChangeCallback(response) {
		console.log('statusChangeCallback');
		console.log(response);

		if (response.status === 'connected') {
			testAPI();
		} else {
			let time = JSON.parse(localStorage.getItem('logged')).time;
			let res = Math.abs(new Date().getTime() - time) / 1000;
			if ((Math.floor(res / 60) % 60) <= 15) {
				let obj = {
					time: new Date().getTime()
				}
				localStorage.setItem('logged', JSON.stringify(obj));

				let dash = JSON.parse(localStorage.getItem('dashboard'));
				dashboard.connection = dash.connection
				dashboard.connection.information.services = null
			}
		}
	}

  function checkLoginState() {
    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
    });
  }

  window.fbAsyncInit = function() {
    FB.init({
      appId      : '1091482254348563',
      cookie     : false,
      xfbml      : true,
      version    : 'v3.1'
    });

    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
    });

  };

  (function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));

	function testAPI() {
		console.log('Welcome!  Fetching your information.... ');
		FB.api('/me', {fields: 'name, email'}, function(fb) {
			$.post('http://dashboard.linaris.fr:8080/is_known', JSON.stringify({
				email: fb.email,
			}), "json")
			.done(function(data) {
					dashboard.connection.information = {
						email: fb.email,
						pseudo: fb.name
					}
					dashboard.connection.used_by = 'facebook'
					data = JSON.parse(data)
					if (data.res === "true") {
						dashboard.connection.connected = true
						dashboard.connection.information.services = data.datas.datas
					}
			 });
		});
	}
</script>


</body>


</html>
