<template>
  <div class="main_page">
	<Logging v-if="!dash.connection.connected"></Logging>
	<div class="inline" v-else>
		<div class="ui grid">
			<div class="two wide column top_bar">
				<topBar></topBar>
			</div>
			<div class="fourteen wide column container_pages">
				<Addservice v-if="dash.pages.index === 3"></Addservice>
				<Addwidget v-if="dash.pages.index === 2 && dash.connection.information.services
						&& dash.connection.information.services.length > 0"></Addwidget>
				<Dashboard v-if="dash.pages.index === 1"></Dashboard>
			</div>
		</div>
	</div>
  </div>
</template>

<script>

import Logging from '@vue/Logging/Logging.vue'
import topBar from '@vue/dashboard/topBar.vue'
import Addservice from '@vue/dashboard/addservice.vue'
import Addwidget from '@vue/dashboard/addwidgets.vue'
import Dashboard from '@vue/dashboard/dashboard.vue'

export default {

	components: { Logging, topBar, Addservice, Dashboard, Addwidget },

	data (){
		return {
			dash: dashboard
		}
	}, methods: {
	}, created() {
		let vm = this
		this.$http.get('http://dashboard.linaris.fr:8080/about.json').then(response => {
			let data = JSON.parse(response.bodyText)
			vm.dash.services = data
		}, response => {});
	}
}

</script>

<style lang="scss" type="text/scss">

.disconnect {
	width: 10vh;
	height: 10vh;
	background: black;
}

.main_page {
	width: 100vw;
	height: 102vh;
	background-size: cover;
	background-image: url(../images/bg1.png)
}

</style>
