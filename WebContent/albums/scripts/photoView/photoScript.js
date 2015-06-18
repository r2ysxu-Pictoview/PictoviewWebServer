var currentPhotoIndex = -1;

$(document).ready(function() {

	console.log($('#albumIdInput').val());
	loadAlbums($('#albumIdInput').val());
	addSwipeEvents();
});

function loadAlbums(albumId) {
	getAlbumsResponse(albumId);
}

function addSwipeEvents() {
	var swiper = new Hammer(document.getElementById('enlargedPhoto'));
	swiper.on('swipeleft', function(event) {
		getNextOriginalImage();
	});
	swiper.on('swiperight', function(event) {
		getPrevOriginalImage();
	});
	swiper.on('swipedown', function(event) {
		closeModal('imageModal');
	});
}

function getOriginalImage(index, id) {
	currentPhotoIndex = index;
	var originalImage = document.getElementById("enlargedPhoto");
	originalImage.src = "images.do" + "?photoid=" + id;
	var modalImageDiv = document.getElementById("imageModal");
	modalImageDiv.className = "modalDialog opaque showDialog";
	originalImage.onload = function() {
		
		$('#enlargedPhoto').removeClass('fullImageWide fullImageTall');
		 if (originalImage.clientWidth > originalImage.clientHeight) {
			$('#enlargedPhoto').addClass('fullImageWide');
		} else {
			$('#enlargedPhoto').addClass('fullImageTall');
		}
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

function closeModal(modal) {
	var modalImageDiv = document.getElementById(modal);
	modalImageDiv.className = "modalDialog opaque";
}

function showUploadForm() {
	var modalImageDiv = document.getElementById("uploadModal");
	modalImageDiv.className = "modalDialog opaque showDialog";
}