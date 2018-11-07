<template>
	<div class="youtube_widget">
		<div class="infos inline">
			<div class="ui grid infos no-marg">
				<div class="sixteen wide column inline board">
					<div class="ui grid">
						<div class="eight wide column align inline name no-pad">
							<div class="ui grid">
								<div class="three wide column align">
									<i class="flag" :class="datas.country"></i>
								</div>
								<div class="eleven wide column align-left inline">
									<div class="inline">
										{{ widget.params[0].value }}
									</div>
								</div>
							</div>
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
			<div class="ui grid inline info no-marg">
				<div class="five wide column inline no-pad">
					<img class="favicon" v-if="isUserName()" :src="datas.thumbnail.url" alt="">
					<img :src="datas.miniature" class="favicon" style="height: 78%" v-else alt="">
				</div>
				<div v-if="isUserName()" class="ten wide column inline">
					<div class="ui grid">
						<div class="sixteen wide column align">
							<div class="ui grid">
								<div class="eight wide column align">
									<div class="title">
										Abonnées
									</div>
									<div class="value">
										{{formatNumber(datas.subs)}}
									</div>
								</div>
								<div class="eight wide column align">
									<div class="title">
										Videos
									</div>
									<div class="value">
										{{datas.videos}}
									</div>
								</div>
							</div>
						</div>
						<div class="sixteen wide column align">
							<div class="title">
								Vues
							</div>
							<div class="value">
								{{formatNumber(datas.views)}}
							</div>
						</div>
					</div>
				</div>
				<div v-else class="ten wide column inline">
					<div class="ui grid">
						<div class="sixteen wide column align">
							<div class="ui grid">
								<div class="eight wide column align">
									<div class="title">
										Like
									</div>
									<div class="value">
										{{formatNumber(datas.like)}}
									</div>
								</div>
								<div class="eight wide column align">
									<div class="title">
										Comments
									</div>
									<div class="value">
										{{formatNumber(datas.comments)}}
									</div>
								</div>
							</div>
						</div>
						<div class="sixteen wide column align">
							<div class="title">
								Vues
							</div>
							<div class="value">
								{{formatNumber(datas.views)}}
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
		openPopup() {
			$('#' + this.widget.id).modal('show');
		},
		isUserName() {
			return this.widget.params[0].name === 'Username'
		},
		set() {
			this.datas = this.parent.lookForNewParams(this.widget).datas
		},
		formatNumber(number) {
			let itl = null
			try {
				itl = new Intl.NumberFormat(
					'fr-FR',
					{
						style: 'decimal'
					}
				).format(number);
			}
			catch(error) {
				itl = number
			}

			return itl
		},
	}, created() {
		this.dash.mdm[this.widget.id] = this.set
		this.datas = this.widget.datas
	}
}

</script>

<style lang="scss" type="text/scss">

.youtube_widget {

	.infos {
		width: 100%;
	}

	.info {
		width: 100%;
		height: 100%
	}

	.flag {
		padding: 1vh;
		padding: 0.4vh 1vh;
	}

	.title {
		font-size: 1em;
		font-weight: 600;
	}
	.value {
		font-size: 1.2em;
		font-weight: 700;
	}
}

</style>
