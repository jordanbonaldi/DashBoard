<template>
	<div class="tvshow_widget">
		<div class="infos inline">
			<div class="ui grid infos no-marg">
				<div class="sixteen wide column inline board">
					<div class="ui grid">
						<div class="eight wide column align inline name no-pad">
							<div class="ui grid">
								<div class="three wide column align">
									<i class="flag" :class="datas.original_language"></i>
								</div>
								<div class="eleven wide column align-left inline">
									<div class="inline">
										{{ datas.original_name }}
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
				<div class="sixteen wide column inline no-pad">
					<img class="poster" :src="'https://image.tmdb.org/t/p/w500/' + datas.backdrop_path" alt="">
				</div>
				<div class="sixteen wide column inline no-pad relative top-2">
					<div class="ui grid">
						<div class="sixteen wide column align">
							<div class="ui grid">
								<div class="eight wide column align">
									<div class="title">
										Saisons
									</div>
									<div class="value">
										{{datas.number_of_seasons}}
									</div>
								</div>
								<div class="eight wide column align">
									<div class="title">
										Popularité
									</div>
									<div class="value">
										{{datas.popularity}}
									</div>
								</div>
							</div>
						</div>
						<div class="sixteen wide column align">
							<div class="title">
								Prochain épisode
							</div>
							<div class="value">
								<div class="" v-if="datas.next_episode_to_air">
									{{ formatDate(datas.next_episode_to_air.air_date) }}
								</div>
								<div class="" v-else>
									Non programmé.
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

	props: {
		widget: Object
	},
	data (){
		return {
			dash: dashboard,
			datas: null
		}
	}, methods: {
		formatDate(w) {
			let date = new Date(Date.parse(w));
			return date.toDateString();
		},
		openPopup() {
			$('#' + this.widget.id).modal('show');
		},
	}, created() {
		this.datas = this.widget.datas
	}
}

</script>

<style lang="scss" type="text/scss">

.tvshow_widget {

	.top-2{
		top: 2vh;
	}

	.infos {
		width: 100%;
	}

	.info {
		width: 100%;
		height: 100%
	}

	.poster {
		width: 100%
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
