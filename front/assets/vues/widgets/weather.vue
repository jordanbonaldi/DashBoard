<template>
	<div class="weather_widget">
		<div class="info inline">
			<div class="ui grid infos no-marg">
				<div class="sixteen wide column inline board">
					<div class="ui grid">
						<div class="eight wide column align inline name no-pad">
							<div class="ui grid">
								<div class="three wide column align">
									<i class="flag" :class="datas.sys.country"></i>
								</div>
								<div class="eleven wide column align-left inline">
									<div class="inline">
										Climat
									</div>
									<div class="inline">
										{{ datas.name }}
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
			<div class="ui grid takeall inline">
				<div class="sixteen wide column">
					<div class="ui grid align">
						<div class="three column row">
							<div class="column">
								<div class="title">
									Température
								</div>
								<div class="value">
									{{ convertKelvin(datas.main.temp) }}°C
								</div>
							</div>
							<div class="column akf">
								<img :src="'http://openweathermap.org/img/w/'+ datas.weather[0].icon +'.png'" alt="">
								<div class="akd">
									{{ datas.weather[0].main }}
								</div>
							</div>
							<div class="column">
								<div class="title">
									Humidité
								</div>
								<div class="value">
									{{ datas.main.humidity }}%
								</div>
							</div>
						 </div>
					</div>
				</div>
				<div class="sixteen wide column afg">
					<div class="ui grid align">
						<div class="three column row">
							<div class="column">
								<div class="sun risingsun">
									<img src="../../images/sunset.png" alt="">
								</div>
								<div class="value">
									{{ convertMillis(datas.sys.sunset) }}
								</div>
							</div>
							<div class="column">

							</div>
							<div class="column">
								<div class="sun risingsun">
									<img src="../../images/rising.png" alt="">
								</div>
								<div class="value">
									{{ convertMillis(datas.sys.sunrise) }}
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
		convertMillis(millis) {
			let date = new Date(millis * 1000);
			return date.toLocaleTimeString();
		},
		convertKelvin(a) {
			return new Number(a - 273).toFixed(2);
		},
		openPopup() {
			$('#' + this.widget.id).modal('show');
		}
	}, created() {
		this.datas = this.widget.datas
	}
}

</script>

<style lang="scss" type="text/scss">

.weather_widget {
	background: #ffffffe0;
	width: 100%;
	height: 100%;

	.takeall {
		width: 100%
	}

	.info {
		width: 100%;
		height: 100%;
	}

	.flag {
		padding: 1vh;
		padding: 0.4vh 1vh;
	}

	.board {
		border-bottom: solid 1px gray;

		.name {
			font-size: .6em;
			font-weight: 700;
			padding: 1vh !important;
		}

		.mod {
			font-size: 1em;
			padding: 1vh !important;
		}
	}
	.title {
		font-size: 1em;
		font-weight: 500;
	}
	.taux {
		font-size: 1.2em;
		font-weight: 600;
	}
	.sun img {
		width: 4vh;
	}
	.value {
		font-size: 1.2em;
		font-weight: 600;
	}
	.akf {
		position: relative;
		top: 2vh;
	}
	.afg {
		position: relative;
		top: -3vh;
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
