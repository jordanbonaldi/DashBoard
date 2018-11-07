<template>
	<div class="widget_page inline">
		<div class="ui modal create" v-if="w !== null" :id="sv.name">
			<i class="close icon"></i>
			<div class="header">
				Paramétrage de {{ w.name }}
			</div>
			<div class="parameters align">
				<div class="desc">
					{{ w.description }}
				</div>
				<div class="inputs" v-for="p in w.params" v-if="p.name !== 'btn'">
					<div class="info block">
						{{ p.name }}:
					</div>
					<div class="ui small icon input">
						<input type="text" :name="p.name" v-model="p.value">
					</div>
				</div>
				<div class="inputs refresh">
					<div class="info block">
						Temps de raffraichissement
					</div>
					<div class="ui small icon input">
						<input type="text" :name="w.refresh" v-model="w.refresh">
					</div>
				</div>
			</div>
			<div class="actions">
				<div class="ui button" @click="cancel">Annuler</div>
				<div class="ui button" @click="send()">Valider</div>
			</div>
		</div>
		<div class="ui modal modify" v-if="w !== null" :id="sv.name">
			<i class="close icon"></i>
			<div class="header">
				Paramétrage de {{ w.name }}
			</div>
			<div class="parameters align">
				<div class="desc">
					{{ w.description }}
				</div>
				<div class="inputs" v-for="p in w.params">
					<div class="info block">
						{{ p.name }}:
					</div>
					<div class="ui small icon input">
						<input type="text" :name="p.name" v-model="p.value">
					</div>
				</div>
				<div class="inputs refresh">
					<div class="info block">
						Temps de raffraichissement
					</div>
					<div class="ui small icon input">
						<input type="text" :name="w.refresh" v-model="w.refresh">
					</div>
				</div>
			</div>
			<div class="actions">
				<div class="ui button" @click="cancel">Annuler</div>
				<div class="ui button" @click="sendModify()">Valider la modification</div>
			</div>
		</div>
		<div class="all_services centered-window-40 relative">
			<div class="ui grid" v-if="selected">
				<div class="sixteen wide column align-left">
					<i v-if="!modify" class="icc huge icon reply clicked" @click="selected = false"></i>
					<i v-if="modify" class="icc huge icon reply clicked" @click="modify = false"></i>
				</div>
			</div>
			<div v-if="!selected" class="card_services inline" v-for="service in tab">
				<div class="ui grid">
					<div class="sixteen wide column">
						<div class="remove align-right">
							<i class="close icon large red clicked" @click="removeService(service.name)"></i>
						</div>
						<div class="title align clicked" @click="getWidgetFor(service)">
							{{service.name}}
						</div>
					</div>
				</div>
			</div>
			<div v-if="selected && widget_selected && !modify" class="card_services clicked inline"
				v-for="widget in widget_selected.widgets">
				<div class="ui grid">
					<div class="sixteen wide column align">
						<div class="title align" @click="openPopup(widget)">
							{{widget.name}}
						</div>

						<div class="modify act relative" v-if="getWidgetsOfN(widget) > 0">
							Actuellement : {{getWidgetsOfN(widget)}}
						</div>
						<div class="ui button mm relative" @click="openPopup(widget)">
							Ajouter
						</div>
						<div class="ui button mm relative" v-if="getWidgetsOfN(widget) > 0" @click="modify = true">
							Modifier
						</div>
					</div>
				</div>
			</div>
			<div v-if="selected && modify" class="card_services inline"
				v-for="ww in getWidgetsOf()">
				<div class="ui grid">
					<div class="sixteen wide column align">
						<div class="remove align-right">
							<i class="close icon large red clicked" @click="removeWidget(ww)"></i>
						</div>
						<div class="title clicked align" @click="openPopup(ww)">
							{{ww.name}}
							<p>Id : {{ww.id}}</p>
							<div class="params" v-for="p in ww.params" v-if="p.name !== 'btn'">
								<div class="info block">
									{{ p.name }}: {{p.value}}
								</div>
							</div>
						</div>
						<div class="ui mm relative button" @click="openPopup(ww)">
							Cliquer pour modifier
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
			selected: false,
			sv: null,
			modify: false,
			widget_selected: {
				widgets: []
			},
			tab: [],
			w: null
		}
	}, created() {
			this.tab = this.dash.connection.information.services;
	},
	methods: {
		getWidgetFor(service) {
			this.sv = service
			this.widget_selected = this.dash.services.server.services.find(e => e.name === service.name);
			this.selected = true
		},
		removeService(name) {
			let vm = this
			let obj = {
				email: this.dash.connection.information.email,
				service: name
			}
			console.log("Removing " + name);
			$.post('http://dashboard.linaris.fr:8080/removeservices',
			JSON.stringify(obj), "json").done(function(data) {
				vm.dash.connection.information.services = JSON.parse(data).datas
				vm.tab = vm.dash.connection.information.services
				console.log(vm.dash.connection.information.services)
				vm.dash.topBar.refresh()
			});
		},
		removeWidget(ww) {
			let vm = this
			let obj = {
				email: this.dash.connection.information.email,
				widget: ww
			}
			console.log("Removing " + name);
			console.log(obj)
			$.post('http://dashboard.linaris.fr:8080/removewidget',
			JSON.stringify(obj), "json").done(function(data) {
				vm.dash.connection.information.services = JSON.parse(data).datas
				vm.tab = vm.dash.connection.information.services
				if (vm.getWidgetsOf().length == 0)
					vm.modify = false
				vm.dash.topBar.refresh()
			});
		},
		getWidgetsOf() {
			if (this.sv === null)
				return [];

			let srv = this.dash.connection.information.services.find(e => e.name == this.sv.name);
			return srv.widgets;
		},
		getWidgetsOfN(w) {
			if (this.sv === null)
				return [];

			let ww = 0;

			let srv = this.dash.connection.information.services.find(e => e.name == this.sv.name);

			srv.widgets.forEach(e => {
				if (e.name === w.name)
					ww += 1;
			})

			return ww;
		},
		send() {
			let vm = this;
			let obj = {
				email: this.dash.connection.information.email,
				widget: this.w
			}
			console.log(this.w)
			console.log("Trying to add widget " + this.w.name);
			$.post('http://dashboard.linaris.fr:8080/addwidgets',
			JSON.stringify(obj), "json").done(function(data) {
				let olddatas = vm.dash.connection.information.services
				vm.dash.connection.information.services = JSON.parse(data).datas
				vm.dash.connection.information.services.forEach(e=>{
					e.widgets.forEach(a => {
						olddatas.forEach(ee=>{
							ee.widgets.forEach(aa=>{
								if (aa.id == a.id)
									a.datas = aa.datas;
							})
						})
					})
				})
				vm.tab = vm.dash.connection.information.services
				vm.dash.topBar.refresh()
				let a = null;
				vm.tab.forEach(s => {
					s.widgets.forEach(e => {
						if (e.id === s.last)
							a = e
					})
				})
				console.log(a)
				vm.dash.refresher(a);
				vm.dash.dashboardrfc();
			});
			this.cancel()
		},
		sendModify() {
			let vm = this;
			let obj = {
				email: this.dash.connection.information.email,
				widget: this.w
			}
			console.log(this.w)
			console.log("Trying to modify widget " + this.w.name);
			$.post('http://dashboard.linaris.fr:8080/modifywidget',
			JSON.stringify(obj), "json").done(function(data) {
				vm.dash.connection.information.services = JSON.parse(data).datas
				console.log(vm.dash.connection.information.services)
				vm.dash.topBar.refresh()
				if (vm.dash.intervals[vm.w.id])
					clearInterval(vm.dash.intervals[vm.w.id])
				vm.dash.refresher(vm.w)
			});
			this.cancel()
		},
		cancel() {
			if (this.modify)
				$('#' + this.sv.name + '.modify').modal('hide');
			else
				$('#' + this.sv.name + '.create').modal('hide');
		},
		openPopup(widget) {
			this.w = widget
			if (this.modify)
				$('#' + this.sv.name + '.modify').modal('show');
			else
				$('#' + this.sv.name + '.create').modal('show');
		}
	}
}

</script>

<style lang="scss" type="text/scss">
.widget_page {
	width: 100vw;
}

.icc {
	top: -10vh;
	position: relative;
	color: #d6d6d6;
	margin: 3vh;
}

.icc:hover {
	color: white;
}

.mm {
	top: 2vh;
}

.act {
	padding: 2vh;
	position: relative;
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

.parameters {
	.desc {
		font-size: 1.2em;
		font-weight: 700;
		padding: 3vh;
	}

	.inputs {
		padding: 2vh 0vh 4vh 0vh;
		.info {
			font-size: 1em;
			padding: 1vh;
			padding: 3vh;
		}
	}
}

.card_services:hover {
	padding: 4.2vh;
	border: solid blue 1px;
}
</style>
