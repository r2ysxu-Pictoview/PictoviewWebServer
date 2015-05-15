$(document).ready(function() {
	var photoId = $.urlParam('photoId');
	createPhotos(photoId);
});

function createPhotos(id) {
	// Create Image
	var image = document.createElement("img");
	image.src = "/PictureViewerWebServer/web/images?" + "photoId=" + id;

	// Append Elements
	$("#photo").append(image);
}