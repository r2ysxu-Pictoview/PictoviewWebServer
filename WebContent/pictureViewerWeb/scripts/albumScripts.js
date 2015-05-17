var photoJson;
var albumJson;
var currentPhotoIndex = 0;

$(document).ready(function() {
	getAlbumsResponse();
	getPhotoResponse();
});

function getAlbumsResponse() {
	var parentId = $.urlParam('albumId');
	if (parentId == null) {
		parentId = 0;
	}
	$.get('galleryServlet',{
		"parentId" : parentId
	}, function(response) {
		populateAlbums(response);
	}).fail(function() {
		alert("Response Failed");
	});
}

function populateAlbums(json) {
	albumJson = json;
	$.each(json, function(i, value) {
		var albumA = generateAlbumDiv(value.id, value.coverId, value.name)

		$("#albums").append(albumA);
	});
}

function generateAlbumDiv(id, coverId, name) {
	var albumAnchor = document.createElement("a");
	var albumDiv = document.createElement("div");
	var albumInfo = document.createElement("div");
	var albumCover = document.createElement("img");
	var albumNameDiv = document.createElement("div");
	var albumName = document.createElement("h3");
	var albumSubtitle = document.createElement("h4");
	var albumExpand = document.createElement("button");
	
	// Setting class
	albumDiv.className="albumDiv";
	albumInfo.className="albumInfo";
	albumCover.className="albumCover";
	albumNameDiv.className="albumName";
	albumExpand.className="albumExpand";
	
	// Set Cover image
	if (coverId != 0) {
		albumCover.src = "/PictureViewerWebServer/web/images/thumbnail?" + "photoId="
			+ coverId + "&albumId=" + id;
	} else {
		albumCover.src = "resources/images/noimage.jpg";
	}
	albumAnchor.href = "galleryView.html" + "?albumId=" + id;
	
	albumName.innerText = name;
	albumExpand.innerText =" ";
	albumExpand.onclick = function() {
		getAdditionAlbumInfo();
	}
	
	// Append elements
	albumNameDiv.appendChild(albumName);
	albumNameDiv.appendChild(albumSubtitle);
	albumInfo.appendChild(albumCover);
	albumInfo.appendChild(albumNameDiv);
	albumAnchor.appendChild(albumInfo);
	albumDiv.appendChild(albumAnchor);
	albumDiv.appendChild(albumExpand);
	
	return albumDiv;
}

function createAlbum() {
	$.get('galleryCreateServlet', function(response) {
		alert('Album Created');
	}).fail(function() {
		alert("Response Failed");
	});
}

function getAdditionAlbumInfo() {
	alert("Additional Info");
}

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