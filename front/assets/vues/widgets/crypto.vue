<template>
	<div class="crypto_widget">
		<div class="infos inline">
			<div class="ui grid infos no-marg">
				<div class="sixteen wide column inline board">
					<div class="ui grid">
						<div class="eight wide column align inline name no-pad">
							{{ datas.Data.AggregatedData.FROMSYMBOL }}
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
				<div class="sixteen wide column">
					<div class="ui grid">
						<div class="eight wide column align">
							<div class="title">
								Dernier prix
							</div>
							<div class="block taux">
								{{ formatNumber(datas.Data.AggregatedData.PRICE)}}
							</div>
						</div>
						<div class="eight wide column align">
							<div class="pct">
								<div class="title">
									Derniere 24h
								</div>
								<div class="block taux">
									{{ datas.Data.AggregatedData.CHANGEPCT24HOUR.toFixed(2) }} %
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="sixteen wide column">
					<div class="ui grid">
						<div class="eight wide column align">
								<div class="title">
									Volume 24h
								</div>
								<div class="block taux">
									{{ formatNumber(datas.Data.AggregatedData.VOLUME24HOUR.toFixed(0))}}
								</div>
						</div>
						<div class="eight wide column align">
							<div class="title">
								Derniere 24H
							</div>
							<div class="block taux">
								{{ formatNumber(datas.Data.AggregatedData.HIGH24HOUR)}}
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
		getFiat() {
			return this.widget.params[1].value
		},
		formatNumber(number) {
			let itl = null
			try {
				itl = new Intl.NumberFormat(
					'fr-FR',
					{
						style: 'currency',
						currency: this.getFiat()
					}
				).format(number);
			}
			catch(error) {
				itl = number + " " + this.getFiat()
			}

			return itl
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


.crypto_widget {
	background: #ffffffe0;
	width: 100%;
	height: 100%;

	.ll {
		width: 100%
	}

	img.favicon {
		width: 90%;
	}

	.board {
		border-bottom: solid 1px gray;

		.name {
			font-size: 1.1em;
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
