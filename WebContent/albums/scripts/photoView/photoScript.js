var currentPhotoIndex = -1;

$(document).ready(function() {
	loadAlbums($('#albumIdInput').val());
	addSwipeEvents($('#enlargedPhoto')[0]);
});

function loadAlbums(albumId) {
	getAlbumsResponse(albumId);
}

function addSwipeEvents(element) {
	var swiper = new Hammer(element);
	swiper.on('swipeleft', function(event) {
		getNextOriginalImage();
	});
	swiper.on('swiperight', function(event) {
		getPrevOriginalImage();
	});
	swiper.on('swipedown', function(event) {
		closePhotoModal('imageModal');
	});
}

function loadImage(id, cIndex) {
	var originalImage = document.getElementById("enlargedPhoto");
	originalImage.src = "images.do" + "?photoid=" + id;

	originalImage.onload = function() {
		setImageDisplaySize($('#enlargedPhoto'), originalImage);
	}
}

function getOriginalImage(index, id) {
	currentPhotoIndex = index;
	loadImage(id, 0);
	var modalImageDiv = document.getElementById("imageModal");
	modalImageDiv.className = "modalDialog opaque showDialog";
}

function setImageDisplaySize(jElement, originalImage) {
	jElement.removeClass('fullImageWide fullImageTall');
	 if (originalImage.clientWidth > originalImage.clientHeight) {
		jElement.addClass('fullImageWide');
	} else {
		jElement.addClass('fullImageTall');
	}
}

function getNextOriginalImage() {
	var photoCount = $('#photoCount')[0].innerText;
	if (currentPhotoIndex < photoCount - 1) {
		var nextIndex = currentPhotoIndex + 1;
		var imgElem = $('#photos')[0].children[nextIndex];
		var id = imgElem.id.split("-")[1];
		getOriginalImage(nextIndex, id);
	}
}

function getPrevOriginalImage() {
	if (currentPhotoIndex > 0) {
		var prevIndex = currentPhotoIndex - 1;
		var id = $('#photos')[0].children[prevIndex].id.split("-")[1];
		getOriginalImage(prevIndex, id);
	}
}

function closePhotoModal(modal) {
	var modalImageDiv = document.getElementById(modal);
	modalImageDiv.className = "modalDialog opaque";
}

function showUploadForm() {
	var modalImageDiv = document.getElementById("uploadModal");
	modalImageDiv.className = "modalDialog opaque showDialog";
}