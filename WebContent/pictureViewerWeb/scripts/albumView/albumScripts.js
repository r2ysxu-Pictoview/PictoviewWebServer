var albumJson;
var lastAccessedAlbum = -1;

$(document).ready(function() {
	getAlbumsResponse();
	getPhotoResponse();
});

/* Fetch Album Related */

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
		var albumA = generateAlbumRow(value.id, value.coverId, value.name)

		$("#albumsTable").append(albumA);
	});
}

function generateAlbumRow(id, coverId, name) {
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
		albumCover.src = "/PictureViewerWebServer/web/images/thumbnail?" + "photoId="
			+ coverId + "&albumId=" + id;
	} else {
		albumCover.src = "resources/images/noimage.jpg";
	}
	albumAnchor.href = "galleryView.html" + "?albumId=" + id;
	
	albumName.innerText = name;
	albumExpand.onclick = function() {
		getAdditionAlbumInfo(albumRow.rowIndex);
	};
	
	// Append elements
	albumNameDiv.appendChild(albumName);
	albumNameDiv.appendChild(albumSubtitle);
	albumInfo.appendChild(albumCover);
	albumInfo.appendChild(albumNameDiv);
	albumAnchor.appendChild(albumInfo)
	albumCell.appendChild(albumAnchor);
	albumCell.appendChild(albumExpand);
	albumRow.appendChild(albumCell);
	
	return albumRow;
}

/* Create Album Related */

function createAlbum() {
	$.get('galleryCreateServlet', function(response) {
		alert('Album Created');
	}).fail(function() {
		alert("Response Failed");
	});
}