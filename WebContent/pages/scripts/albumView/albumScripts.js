var albumJson;
var lastAccessedAlbum = -1;

var photoJson;
var currentPhotoIndex = 0;

$(document).ready(function() {
	getAlbumsResponse();
});

/* Fetch Album Related */

function getAlbumsResponse() {
	var albumId = $.urlParam('albumId');
	if (albumId == null) {
		albumId = 0;
	}
	$.get('/PictureViewerWebServer/albums/get.do',{
		"albumId" : albumId
	}, function(response) {
		onInitialResponse(JSON.parse(response));
	}).fail(function() {
		alert("Response Failed");
	});
}

function onInitialResponse(response) {
	console.log(response);
	populateAlbums(response.subAlbums);
	populatePhotos(response.photos);
	allUserTags = response.categories;
}

function populateAlbums(json) {
	albumJson = json;
	$.each(json, function(i, value) {
		var albumA = generateAlbumRow(value.id, value.coverId, value.name, value.subtitle)
		$("#albumsTable").append(albumA);
	});
}

function generateAlbumRow(id, coverId, name, subtitle) {
	var albumRow = document.createElement("tr");
	var albumAnchor = document.createElement("a");
	var albumCell = document.createElement("td");
	var albumInfo = document.createElement("div");
	var albumCover = document.createElement("img");
	var albumNameDiv = document.createElement("div");
	var albumName = document.createElement("h3");
	var albumSubtitle = document.createElement("h4");
	var albumExpand = document.createElement("button");
	
	// Setting class
	albumCell.className="albumCell";
	albumInfo.className="albumInfo";
	albumCover.className="albumCover";
	albumNameDiv.className="albumName";
	albumExpand.className="albumExpand";
	
	// Set Cover image
	if (coverId != 0) {
		albumCover.src = "/PictureViewerWebServer/images/thumbnail.do" + "?photoid="
			+ coverId;
	} else {
		albumCover.src = "resources/images/noimage.jpg";
	}
	albumAnchor.href = "albumView.html" + "?albumId=" + id;
	
	albumName.innerText = name;
	albumSubtitle.innerText = subtitle;
	albumExpand.onclick = function() {
		getAdditionAlbumInfo(albumRow.rowIndex);
	};
	
	// Append elements
	albumAnchor.appendChild(albumName);
	albumNameDiv.appendChild(albumAnchor);
	albumNameDiv.appendChild(albumSubtitle);
	albumInfo.appendChild(albumCover);
	albumInfo.appendChild(albumNameDiv);
	albumCell.appendChild(albumInfo);
	albumCell.appendChild(albumExpand);
	albumRow.appendChild(albumCell);
	
	return albumRow;
}

/* Album Photo Related */

function generatePhotoDiv(index, id) {
	var photoDiv = document.createElement("span");

	// Create Image
	var image = document.createElement("img");
	image.src = "/PictureViewerWebServer/images/thumbnail.do" + "?photoid=" + id;
	image.className = "imageThumbnail";
	
	photoDiv.onclick = function() {
		getOriginalImage(index, id)
	};

	// Append Elements
	photoDiv.appendChild(image);
	return photoDiv;
}

function populatePhotos(json) {
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
	originalImage.src = "/PictureViewerWebServer/images.do" + "?photoid=" + id;
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

/* Create Album Related */

function createAlbum() {
	$.get('galleryCreateServlet', function(response) {
		alert('Album Created');
	}).fail(function() {
		alert("Response Failed");
	});
}