class Dashboard {
	constructor() {
		this.connection = {
			used_by: "none",
			connected: false,
			information: null,
			known: false,
			register: false,
			widgets: 0,
			services: 0
		};
		this.pages = {
			index: 1
		};
		this.services = {
			widgets: null
		}
		this.refresher = null
		this.topBar = null
		this.times = 0
		this.rfc = []
		this.intervals = []
		this.dashboardrfc = null
		this.mdm = []
	}
}

var dashboard = new Dashboard();
