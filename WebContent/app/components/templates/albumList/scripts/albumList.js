function AlbumList(listEl) {
	this.listEl = listEl
	this.albums = []
	this.selectedIndex = 0

	this.loadData = function(parentId, data) {
		this.albums[parentId] = data || []
	}
	
	this.hasLoadedData = function(id) {
		return !this.albums[id]
	}
}