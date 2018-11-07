<template>
	<div class="service_page inline">
		<div class="all_services centered-window-40 relative">
			<div class="card_services clicked inline" v-bind:key="service.name"
			v-for="service in tab"
			@click="sendService(service.name)">
				<div class="ui grid">
					<div class="sixteen wide column">
						<div class="title align">
							{{service.name}}
						</div>
						<div class="widgets_nb align">
							{{ "Widgets: " + service.widgets.length }}
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
export default {

	data (){
		return {
			dash: dashboard,
			tab: []
		}
	}, methods: {
		getTab() {
			return this.dash.services.server.services.filter(e => !this.contains(e.name));
		},
		contains(name) {
			console.log(this.dash.connection.information.services)
			let o = this.dash.connection.information.services.find(e => e.name === name);
			console.log(o)
			return o !== undefined
		},
		sendService(name) {
			let vm = this
			let obj = {
				email: this.dash.connection.information.email,
				service: name
			}
			console.log("try to add " + name)
			$.post('http://dashboard.linaris.fr:8080/addservices',
			JSON.stringify(obj), "json").done(function(data) {
				vm.dash.connection.information.services = JSON.parse(data).datas
				console.log(vm.dash.connection.information.services);
				vm.tab = vm.getTab()
				vm.dash.topBar.refresh()
			});
		}
	}, created() {
		this.tab = this.getTab();
		this.dash.topBar.refresh()
	}
}

</script>

<style lang="scss" type="text/scss">
.service_page {
	width: 100vw;
}

.card_services {
	padding: 4vh;
	background: #efefefd1;
	margin: 4vh;
	border-radius: 5px;
	width: 18vw;

	.title{
		font-size: 1.2em;
		font-weight: 700;
	}
	.widgets_nb{
		margin-top: 1vh;
		font-size: .8em;
		font-weight: 700;
	}
}

.card_services:hover {
	padding: 4.2vh;
	border: solid blue 1px;
}
</style>
