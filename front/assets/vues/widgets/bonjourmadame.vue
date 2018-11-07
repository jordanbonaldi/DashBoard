<template>
	<div class="bjr_widget">
		<div class="info inline">
			<div class="ui grid infos no-marg">
				<div class="sixteen wide column inline board">
					<div class="ui grid">
						<div class="eight wide column align inline name no-pad">
							{{ datas.title }}
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
			<div class="ui grid inline mmg">
				<img :src="datas.url" alt="">
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


.bjr_widget {
	background: #ffffffe0;
	width: 100%;
	height: 100%;

	img {
		width: 100%;
		height: 100%;
		object-fit: cover;
	}

	.mmg {
		height: 59vh;
		margin-top: 0 !important;
	}

	.info {
		height: 100%
	}

	.board {
		border-bottom: solid 1px gray;

		.name {
			font-size: .8em;
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
