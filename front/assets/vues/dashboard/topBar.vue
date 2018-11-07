<template>
	<div class="top_bar">
		<div class="ui grid">
			<div class="sixteen wide column no-pad">
				<div class="a_btn align clicked" @click="disconnect">
					Deconnexion
				</div>
			</div>
			<div class="relative centered-window-35 no-pad">
				<div class="ui grid">
					<div class="sixteen wide column">
						<div class="a_btn align clicked" :class="{selected: dash.pages.index === 1}" @click="dash.pages.index = 1, update()">
							DashBoard
						</div>
					</div>
					<div class="sixteen wide column">
						<div class="a_btn align clicked" v-if="widgets" :class="{selected: dash.pages.index === 2}" @click="dash.pages.index = 2, update()">
							Ajouter un widget
						</div>
					</div>
					<div class="sixteen wide column">
						<div class="a_btn align clicked" v-if="services" :class="{selected: dash.pages.index === 3}" @click="dash.pages.index = 3, update()">
							Ajouter un service
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>

export default {

	components: { },

	data (){
		return {
			dash: dashboard,
			widgets: false,
			services: true
		}
	}, methods: {
		update() {
			let vm = this;
		},
		disconnect() {
			let vm = this
			vm.dash.connection.connected = false;
			vm.dash.connection.information = null;
			vm.dash.connection.used_by = null;
			localStorage.clear();
			FB.logout(function(response) {});
		},
		getTab() {
			return this.dash.services.server.services.filter(e => !this.contains(e.name));
		},
		contains(name) {
			let o = this.dash.connection.information.services.find(e => e.name === name);
			return o !== undefined
		},
		refresh() {
			this.widgets = this.dash.connection.information.services.length > 0
			this.services = this.getTab().length > 0

			if (this.services == false && this.dash.pages.index === 3)
				this.dash.pages.index = 2
			else if (this.widgets == false)
				this.dash.pages.index = 3
		}
	}, created() {
		let vm = this
		this.dash.topBar = vm;
	}
}

</script>

<style lang="scss" type="text/scss">

.top_bar {
	height: 103vh;
	background: #2f2f2f;
	z-index: 100;
}

.a_btn {
	padding: 4vh 1vh 4vh 1vh;
	color: white;
	font-weight: 700;
	font-size: 1.2em;
}

.selected {
	background: #4c4c4c;
}

.a_btn:hover {
	background: #4c4c4c;
}

</style>
