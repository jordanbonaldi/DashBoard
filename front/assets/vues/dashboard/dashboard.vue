<template>
	<div class="dashboard inline" ref="workspace">
		<div class="align" v-if="services === null">
			<div class="ui active centered massive align text inline loader" style="top: 45vh; color:white">
				Chargement...
			</div>
		</div>
		<div class="ss" v-for="s in services" v-else>
			<VueDragResize
			:isActive="true"
			:isResizable="false"
			:w="w.w"
			:h="w.h"
			:x="w.x"
			:y="w.y"
			v-on:dragging="resize(w, $event)"
			v-for="w in getWidgetOf(s)">
				<Widget :service="s" :widget="w" :parent="getVm()">
					<Minecraft v-if="s.name === 'MineCraft'" :parent="getVm()" :widget="w"></Minecraft>
					<Crypto v-if="s.name === 'CryptoCurrency'" :parent="getVm()" :widget="w"></Crypto>
					<Bonjourmadame v-if="s.name === 'BonjourMadame'" :parent="getVm()" :widget="w"></Bonjourmadame>
					<Weather v-if="s.name === 'Weather'" :widget="w"></Weather>
					<Youtube v-if="s.name === 'Youtube'" :widget="w" :parent="getVm()"></Youtube>
					<Tvshow v-if="s.name === 'TvShows'" :widget="w"></Tvshow>
				</Widget>
			</VueDragResize>
		</div>
	</div>
</template>

<script>

import VueDragResize from 'vue-drag-resize'
import Minecraft from '@vue/widgets/minecraft.vue'
import Crypto from '@vue/widgets/crypto.vue'
import Bonjourmadame from '@vue/widgets/bonjourmadame.vue'
import Widget from '@vue/widgets/widget.vue'
import Weather from '@vue/widgets/weather.vue'
import Youtube from '@vue/widgets/youtube.vue'
import Tvshow from '@vue/widgets/tvshow.vue'

export default {

	components: { Minecraft, Crypto, VueDragResize, Widget, Bonjourmadame, Weather, Youtube, Tvshow },

	data (){
		return {
			dash: dashboard,
			services: dashboard.connection.information.services,
			x: 300,
			y: 800
		}
	},
	created() {
		let vm = this;
		this.refresh()
		if (this.dash.refresher !== null)
			return;
		let obj = {
			email: vm.dash.connection.information.email,
		}
		$.post('http://dashboard.linaris.fr:8080/getall', JSON.stringify(obj), "json")
		.done(function(data) {
			vm.dash.connection.information.services = JSON.parse(data).datas
			vm.services = vm.dash.connection.information.services
			vm.services.forEach(e => {
				e.widgets.forEach(w => {
						vm.createInterValFor(w);
				})
			})
			vm.dash.topBar.refresh()
		 });
		this.dash.refresher = this.createInterValFor
		this.dash.dashboardrfc = this.refresh
	}, methods: {
		resize(w, newSize) {
			let vm = this;
			let obj = {
				email: this.dash.connection.information.email,
				widget: w,
				x: newSize.left,
				y: newSize.top,
			}

			w.x = newSize.left
			w.y = newSize.top

			$.post('http://dashboard.linaris.fr:8080/resizewidgets',
			JSON.stringify(obj), "json").done(function(data) {});
		},
		getVm() {
			let vm = this;
			return vm;
		},
		refresh(){
			this.services = this.dash.connection.information.services
		},
		isData(w) {
			return w.datas !== null;
		},
		createInterValFor(w) {
			let vm = this;
			console.log("NEW INTERVAL FOR " + w.name + " OF " + w.refresh)
			vm.callRoute(w);
			vm.refresh()
			let time = w.refresh
			vm.dash.intervals[w.id] = setInterval(() => {
				if (!vm.dash.connection.connected)
					return;
				vm.refresh()
				if (vm.alwaysInside(w)) {
					let a = vm.lookForNewParams(w)
					vm.callRoute(a)
				}
			}, w.refresh * 1000);
		},
		lookForNewParams(w) {
				let wi = null;
				this.services.forEach(e => {
					e.widgets.forEach(wa => {
						if (w.id === wa.id)
							wi = wa;
					});
				})
				return wi;
		},
		setFor(w, d, c) {
			this.services.forEach(e => {
				e.widgets.forEach(wa => {
					if (w.id === wa.id)
						wa[d] = c
				});
			})
		},
		alwaysInside(wid) {
			let inside = false;
			this.services.forEach(e => {
				e.widgets.forEach(wa => {
					if (wid.id === wa.id) {
						inside = true
					}
				});
			})
			return inside;
		},
		getWidgetById(w) {
			let wi = null
			this.services.forEach(e => {
				e.widgets.forEach(wa => {
					if (w.id === wa.id) {
						wi = wa
					}
				});
			})
			return wi
		},
		getWidgetOf(s) {
			return this.services.find(e => e.name === s.name).widgets
		},
		callRoute(widget) {
			let vm = this;
			let obj = {}

			widget.params.forEach(e => {
				obj[e.name] = e.value
			});
			$.post('http://dashboard.linaris.fr:8080' + widget.route,
			JSON.stringify(obj), "json").done(function(data) {
				widget.datas = JSON.parse(data)
				vm.dash.rfc.forEach(e => {
					if (e.id === widget.id) {
						e.rfc = false
						e.fnc()
						if (vm.dash.mdm[widget.id])
							vm.dash.mdm[widget.id]()
					}
				})
				vm.$forceUpdate();
				vm.refresh()
			});
		}
	}
}

</script>

<style lang="scss" type="text/scss">

.widg {
	padding: 2vh
}

.dashboard {
	width: 100vw;
}

</style>
