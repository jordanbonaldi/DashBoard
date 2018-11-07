<template>
	<div class="elem">
		<div class="ui modal" :id="widget.id">
			<i class="close icon"></i>
			<div class="header">
				Paramétrage de {{ widget.name }} - {{ widget.id }}
			</div>
			<div class="parameters align">
				<div class="desc">
					{{ widget.description }}
				</div>
				<div class="inputs" v-for="p in widget.params" v-if="p.name !== 'btn'">
					<div class="info block">
						{{ p.name }}:
					</div>
					<div class="ui small icon input">
						<input type="text" :name="p.name" v-model="p.value">
					</div>
				</div>
				<div class="inputs refresh">
					<div class="info block">
						Temps de rafraichissement
					</div>
					<div class="ui small icon input">
						<input type="text" :name="refresh" v-model="widget.refresh">
					</div>
				</div>
			</div>
			<div class="actions">
				<div class="ui button" @click="cancel">Annuler</div>
				<div class="ui button" @click="sendModify()">Valider la modification</div>
			</div>
		</div>
		<div class="wwddj" v-if="tryMe()">
			<slot></slot>
		</div>
		<div class="inline align ll" v-else>
			<div class="ui grid">
				<div class="sixteen wide column align">
					<div class="ui active centered large align text inline loader">
						Chargement...
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>

export default {

	props: {
		widget: Object,
		service: Object,
		parent: Object,
	},
	data (){
		return {
			dash: dashboard,
			datas: null
		}
	}, methods: {
		tryMe() {
			return this.datas && !this.isRFC()
		},
		refresh() {
			this.datas = this.parent.lookForNewParams(this.widget).datas
			this.$forceUpdate();
		}, getWidgetsOf() {
			if (this.service === null)
				return [];

			let srv = this.dash.connection.information.services.find(e => e.name == this.service.name);
			return srv.widgets;
		}, removeWidget(ww) {
			let vm = this
			let obj = {
				email: this.dash.connection.information.email,
				widget: ww
			}
			$.post('http:/dashboard.linaris.fr:8080/removewidget',
			JSON.stringify(obj), "json").done(function(data) {
				vm.dash.connection.information.services = JSON.parse(data).datas
				vm.tab = vm.dash.connection.information.services
				vm.dash.topBar.refresh()
				vm.parent.refresh()
			});
		},
		isRFC(){
			let i = false
			this.dash.rfc.forEach(e => {
				if (e.id === this.widget.id)
					i = e.rfc
			})
			return i
		},
		sendModify() {
			let vm = this;
			let obj = {
				email: this.dash.connection.information.email,
				widget: this.widget
			}
			console.log("Trying to modify widget " + this.widget.name);
			$.post('http://dashboard.linaris.fr:8080/modifywidget',
			JSON.stringify(obj), "json").done(function(data) {
				vm.dash.rfc.forEach(e => {
					if (e.id === vm.widget.id)
						e.rfc = false
				})
				clearInterval(vm.dash.intervals[vm.widget.id])
				JSON.parse(data).datas.forEach(e => {
					e.widgets.forEach(a => {
						if (a.id === vm.widget.id)
							vm.widget.datas = a.datas
					})
				})
				vm.datas = vm.parent.lookForNewParams(vm.widget).datas
				vm.refresh()
				vm.dash.topBar.refresh()
				vm.parent.refresh()
				vm.dash.refresher(vm.widget)
			});
			this.cancel()
		},
		cancel() {
			$('#' + this.widget.id).modal('hide');
		}
	}, created() {
		this.dash.rfc.push({
			id: this.widget.id,
			fnc: this.refresh,
			rfc: false
		});
		if (this.widget.datas !== undefined ||
				this.widget.datas !== null)
				this.refresh()
	}
}

</script>

<style lang="scss" type="text/scss">

.ll {
	width: 100%;
	height: 100%;
	top: 5vh;
	position: relative;
}

.elem {
	width: 100%;
	height: 100%;
	background: #ffffffe0;
}

.wwddj {
	background: #ffffffe0;
	width: 100%;
	height: 100%;

	img.favicon {
		width: 100%;
	}

	.board {
		border-bottom: solid 1px gray;

		.name {
			font-size: 1em;
			font-weight: 700;
			padding: 1vh !important;
		}

		.mod {
			font-size: 1em;
			padding: 1vh !important;
		}
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

.no-marg {
	margin: 0 !important;
}

</style>
