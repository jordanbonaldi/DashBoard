<template>
	<div class="ui grid relative centered-window-50">
		<div class="sixteen wide column block align" @mouseover="loginDisplay = true" @mouseout="loginDisplay = false">
			<div class="block_connect">
				<div class="sign_in inline">
					<div class="ui negative message" v-show="error">
						<i class="close icon" @click="error = false"></i>
						<div class="header">
							Une erreur c'est produite lors de la connection
						</div>
						<p>{{error_message}}</p>
					</div>
					<div class="users ui grid block align inline relative">
						<div class="cadenas">
							<img src="../../images/bootstrap_logo.png" alt="">
						</div>
					</div>
					<div class="formulaire ui grid block align inline relative" v-if="!register && dash.connection.information === null && dash.connection.register === false">
						<div class="thirteen wide column">
							<div class="ui input">
								<input type="text" placeholder="Adresse email" v-model="email" v-on:keyup="searchForEmail">
							</div>
							<div class="ui input">
								<input type="password" placeholder="Mot de passe" v-model="password">
							</div>
						</div>
						<div class="sixteen wide column">
							<div class="btn_co connect inline align clicked" @click="login">
								Se connecter <p class="inline" v-if="found !== null && found.length > 1">- {{found}}</p>
							</div>
							<div class="sep_or">
								Ou
							</div>
							<div class="btn_co btn-fb connect inline align clicked" @click="login_fb">
								Se connecter avec FaceBook
							</div>
						</div>
					</div>
					<div class="formulaire ui grid block align inline relative" v-if="!register && dash.connection.used_by === 'facebook' && dash.connection.information !== null">
						<div class="thirteen wide column">
							<div class="ui input">
								<input type="password" placeholder="Mot de passe" v-model="password">
							</div>
							<div class="ui input">
								<input type="password" placeholder="Mot de passe" v-model="pass2">
							</div>
						</div>
						<div class="sixteen wide column" v-if="password !== null && password === pass2">
							<div class="btn_co connect inline align clicked" @click="__register()">
								S'enregistrer
							</div>
						</div>
					</div>
					<div class="formulaire ui grid block align inline relative" v-if="register">
						<div class="thirteen wide column">
							<div class="ui input">
								<input type="text" placeholder="Adresse email" v-model="email">
							</div>
							<div class="ui input">
								<input type="text" placeholder="Pseudo" v-model="pseudo">
							</div>
							<div class="ui input">
								<input type="password" placeholder="Mot de passe" v-model="password">
							</div>
						</div>
						<div class="sixteen wide column">
							<div class="sixteen wide column" v-if="password && email && pseudo">
								<div class="btn_co connect inline align clicked" @click="__register()">
									S'enregistrer
								</div>
							</div>
						</div>
					</div>
					<div class="no_account">
						<div class="sixteen wide column">
							<div class="ui grid">
								<div class="sixteen wide column inline align">
									<div class="underline clicked" @click="register = !register">
										{{!register ? "S'enregistrer" : "Se connecter"}}
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>

export default {

	components: {},

	data () {
		return {
			dash: dashboard,
			password: null,
			pass2: null,
			email: null,
			pseudo: null,
			error: false,
			error_message: "",
			register: false,
			found: null
		}
	},
	methods: {
		searchForEmail() {
			let vm = this
			$.post('http://dashboard.linaris.fr:8080/get_pseudo',
			JSON.stringify({email: this.email}), "json").done(function(data) {
				data = JSON.parse(data);
				if (data.pseudo !== "false")
					vm.found = data.pseudo
			});
		},
		login_fb() {
			let vm = this
			FB.login(function(fb){
				testAPI();
			}, {scope: 'public_profile,email'});
		},
		login() {
			let vm = this
			let object = {
				email: this.email,
				password: this.password
			}
			$.post('http://dashboard.linaris.fr:8080/connection',
			JSON.stringify(object), "json").done(function(data) {
				data = JSON.parse(data)
				if (data.res === "true") {
					vm.dash.connection.connected = true
					let obj = {
						time: new Date().getTime()
					}
					vm.dash.connection.information = {
						email: vm.email,
						pseudo: vm.found
					}
					vm.dash.connection.information.services = data.datas.datas
					localStorage.setItem('logged', JSON.stringify(obj));
					console.log(JSON.stringify(vm.dash))
					localStorage.setItem('dashboard', JSON.stringify(vm.dash))
				} else {
					vm.error = true
					vm.error_message = data.message
				}
			});
		},
		__register() {
			let vm = this
			let object = {
				email: this.email,
				pseudo: this.pseudo,
				password: this.password
			}

			console.log("inside registering")

			console.log(object);

			if (vm.dash.connection.used_by === "facebook") {
				object.email = vm.dash.connection.information.email
				object.pseudo = vm.dash.connection.information.name
			}

			$.post('http://dashboard.linaris.fr:8080/create_account',
			JSON.stringify(object), "json").done(function(data) {
				console.log(data);
				data = JSON.parse(data);
				if (data.res === "true") {
					vm.dash.connection.connected = true
					vm.dash.connection.information = {
						services: data.datas.datas,
						email: object.email,
						pseudo: object.pseudo
					}
				} else {
					console.log("error")
					console.log(data.message)
					vm.error = true
					vm.error_message = data.message
				}
			});
		}
	},
	created() {
			console.log(this.dash.connection.error)
	}
}

</script>

<style lang="scss" type="text/scss">

.sign_in {
	width: 55vh;

	.ui.input {
		width: 100%;
		margin-top: 2vh;
	}

	.no_account {
		font-size: 1.1em;
		font-weight: 600;
		padding: 4vh;
		color: #5d5d5d;
	}

	.btn_co {
		padding: 1vh;
		width: 80%;
		background: #ff9191c7;
		color: white;
		font-size: 1.2em;
		font-weight: 700;
	}

	.btn-fb {
		background: #3d6cce;
	}

	.sep_or {
		padding: 1vh;
		color: white;
		font-size: 1em;
		font-weight: 700;
	}

	.users {
		background: rgba(90, 90, 90, 0.45);
		width: 100%;
		border-top-right-radius: 5px;
		border-top-left-radius: 5px;
	}

	.formulaire {
		height: 31vh;
		width: 100%;
		background: rgba(90, 90, 90, 0.29);
		margin-top: -0.4vh !important;
		border-bottom-right-radius: 5px;
		border-bottom-left-radius: 5px;
	}
}

</style>
