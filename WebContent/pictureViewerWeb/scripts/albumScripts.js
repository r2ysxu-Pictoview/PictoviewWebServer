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

function createPhotos(json) {
	$.each(json, function(i, value) {
		var photoA = document.createElement("a");
		var photoDiv = document.createElement("div");
		
		photoA.href = "photoView.html" + "?photoId=" + value.id;

		// Create Image
		var image = document.createElement("img");
		image.src = "/PictureViewerWebServer/web/images/thumbnail?" + "photoId=" + value.id;
		image.style.height = "80px";
		image.style.width = "80px";

		// Append Elements
		photoDiv.appendChild(image);

		photoA.appendChild(photoDiv);

		$("#photos").append(photoA);
	});
}