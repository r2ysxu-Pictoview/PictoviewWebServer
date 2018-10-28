function AlbumList(listEl) {
	this.listEl = listEl
	this.albums = []
	this.selectedIndex = 0

	this.loadData = function(data) {
		this.albums = data || []
		console.log(data)
	}
}