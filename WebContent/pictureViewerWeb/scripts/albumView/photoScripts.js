var photoJson;
var currentPhotoIndex = 0;

/* Album Photo Related */

function getPhotoResponse() {
	var albumid = $.urlParam('albumId');
	if (albumid != null) {
		$.get('albumServlet', {
			"albumId" : albumid
		}, function(response) {
			createPhotos(response);
		}).fail(function() {
			alert("Response Failed");
		});
	}
}

function generatePhotoDiv(index, id) {
	var photoDiv = document.createElement("span");

	// Create Image
	var image = document.createElement("img");
	image.src = "/PictureViewerWebServer/web/images/thumbnail?" + "photoId=" + id;
	image.className = "imageThumbnail";
	
	photoDiv.onclick = function() {
		getOriginalImage(index, id)
	};

	// Append Elements
	photoDiv.appendChild(image);
	return photoDiv;
}

function createPhotos(json) {
	photoJson = json;
	var index = 0;
	$.each(json, function(i, value) {
		var photoDiv = generatePhotoDiv(index, value.id);
		$("#photos").append(photoDiv);
		index++;
	});
}

/* Photo Navigation Related */

function getOriginalImage(index, id) {
	currentPhotoIndex = index;
	var originalImage = document.getElementById("enlargedPhoto");
	originalImage.src = "/PictureViewerWebServer/web/images?" + "photoId=" + id;
	var modalImageDiv = document.getElementById("imageModal");
	modalImageDiv.className = "modalDialog showDialog";
}

function getNextOriginalImage() {
	if (currentPhotoIndex < photoJson.length - 1) {
		var nextIndex = currentPhotoIndex + 1;
		getOriginalImage(nextIndex, photoJson[nextIndex].id);
	}
}

function getPrevOriginalImage() {
	if (currentPhotoIndex > 0) {
		var prevIndex = currentPhotoIndex - 1;
		getOriginalImage(prevIndex, photoJson[prevIndex].id);
	}
}

function closeModal() {
	var modalImageDiv = document.getElementById("imageModal");
	modalImageDiv.className = "modalDialog";
}