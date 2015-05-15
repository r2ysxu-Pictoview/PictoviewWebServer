$(document).ready(function() {
	getPageResponse();
});

function getPageResponse() {
	$.get('web', function(response) {
		createAlbums(response);
	}).fail(function() {
		alert("Response Failed");
	});
}

function createPhotos(json) {
	$.each(json, function(i, value) {
		var albumDiv = document.createElement("div");

		// Create Image
		var image = document.createElement("img");
		image.src = "/PictureViewerWebServer/web/images?" + "photoId="
				+ value.id;
		image.style.height = "120px";
		image.style.width = "120px";

		// Create Title
		var title = document.createElement("div");
		title.innerText = value.name;

		// Append Elements
		albumDiv.appendChild(image);
		albumDiv.appendChild(title);

		$("#photos").append(albumDiv);
	});
}