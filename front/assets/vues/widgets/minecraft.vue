<template>
	<div class="minecraft_widget">
		<div class="infos inline">
			<div class="ui grid infos no-marg">
				<div class="sixteen wide column inline board">
					<div class="ui grid">
						<div class="eight wide column align-left inline name no-pad">
							{{datas.server.name}}
						</div>
						<div class="eight wide column align-right mod no-pad">
							<div class="ui grid">
								<div class="eight wide column">

								</div>
								<div class="eight wide column">
									<i class="ui clicked icon pencil alternate align-right" @click="openPopup()"></i>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="ui grid inline no-marg">
				<div class="five wide column inline no-pad">
					<img class="favicon" :src="datas.favicon" alt="">
				</div>
				<div class="ten wide column inline">
					<div class="ui grid">
						<div class="sixteen wide column">
							<div class="players">
								Joueurs: {{datas.players.now + "/" + datas.players.max}}
							</div>
						</div>
						<div class="sixteen wide column  ip">
							IP: {{getIP() + ":" + getPort()}}
						</div>
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
		parent: Object
	},
	data (){
		return {
			dash: dashboard,
			datas: null
		}
	}, methods: {
		getIP(){
			return this.widget.params[0].value
		}, getPort() {
			return this.widget.params[1].value
		},
		openPopup() {
			$('#' + this.widget.id).modal('show');
		},
		set() {
			this.datas = this.parent.lookForNewParams(this.widget).datas
		}
	}, created() {
		this.dash.mdm[this.widget.id] = this.set
		this.datas = this.widget.datas
	}
}

</script>

<style lang="scss" type="text/scss">

.minecraft_widget {

	.players {
		font-size: 1.2em;
		font-weight: 700;
		padding-top: 1.5vh;
		position: relative;
		top: 2vh;
	}
	.ip {
		font-size: .9em;
	}
}

</style>
