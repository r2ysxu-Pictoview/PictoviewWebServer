function expandAlbum(td) {
	event.stopPropagation();
	var albumId = td.id.split("-")[1];
	getAlbumsResponse(albumId);
	return true;
}

function getAlbumsResponse(albumId) {
	if (albumId == null) {
		albumId = 0;
	}
	$.get('albums/get.do', {
		"albumId" : albumId
	}, function(response) {
		populateAlbums(albumId, JSON.parse(response));
	}).fail(function() {
		alert("Response Failed");
	});
}

function populateAlbums(albumId, json) {
	if ($("#albumSub-" + albumId).is(':empty')) {
		$.each(json, function(i, value) {
			var albumA = generateAlbumRow(value.id, value.coverId, value.name,
					value.subtitle)
			$("#albumSub-" + albumId).append(albumA);
		});
	}
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
	var albumSubTable = document.createElement("table");

	// Setting class
	albumCell.className = "albumCell";
	albumInfo.className = "albumInfo";
	albumCover.className = "albumCover";
	albumNameDiv.className = "albumName";
	albumSubTable.className = "albumsTable";

	albumCell.id = "albumCell-" + id;
	albumSubTable.id = "albumSub-" + id;

	// Set Cover image
	if (coverId != 0) {
		albumCover.src = "/PictureViewerWebServer/images/thumbnail.do"
				+ "?photoid=" + coverId;
	} else {
		albumCover.src = "resources/images/noimage.jpg";
	}
	albumAnchor.href = "photos.do" + "?albumId=" + id;

	albumName.innerText = name;
	albumSubtitle.innerText = subtitle;
	
	albumCell.onclick = function(event) {
		return expandAlbum(albumCell);
	};

	// Append elements
	albumAnchor.appendChild(albumName);
	albumNameDiv.appendChild(albumAnchor);
	albumNameDiv.appendChild(albumSubtitle);
	albumInfo.appendChild(albumCover);
	albumInfo.appendChild(albumNameDiv);
	albumCell.appendChild(albumInfo);
	albumCell.appendChild(albumSubTable);
	albumRow.appendChild(albumCell);

	return albumRow;
}