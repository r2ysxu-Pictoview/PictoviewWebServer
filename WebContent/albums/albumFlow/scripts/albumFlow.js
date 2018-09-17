function AlbumFlow(listEl) {
	this.listEl = listEl
	this.albums = []
	this.selectedIndex = 0

	this.loadData = function(data) {
		this.albums = data || []
		console.log(data)
	}

	this.selectAlbum = function(index) {
		const previous = this.selectedIndex;

		let target = $('.flow_selected');
		let source = this.listEl;
		if (target.length && index >= 0 && index < this.albums.length) {
			if (previous > index) {
				console.log(target.position().left);
				source.scrollLeft(Math.max(0, source.scrollLeft() - (source.width() - target.position().left) + (source.width() / 2)) );
			} else if (previous < index) {
				source.scrollLeft(source.scrollLeft() + target.position().left - target.width());
			}
			this.selectedIndex = index;
		}
	}
}