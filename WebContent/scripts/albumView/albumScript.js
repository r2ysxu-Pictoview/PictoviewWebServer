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
	var albumCell = document.createElement("td");
	
	var albumSubTable = document.createElement("table");

	// Setting class
	albumCell.className = "albumCell";
	albumSubTable.className = "albumsTable";

	albumCell.id = "albumCell-" + id;
	albumSubTable.id = "albumSub-" + id;
	
	albumCell.onclick = function() {
		return expandAlbum(albumCell);
	};

	// Append elements
	albumCell.appendChild(generateAlbumsRowInfo(id, name, subtitle, coverId));
	albumCell.appendChild(generateAlbumsRowTag(id));
	albumCell.appendChild(albumSubTable);
	albumRow.appendChild(albumCell);
	return albumRow;
}

function generateAlbumsRowInfo(id, name, subtitle, coverId) {
	var albumInfo = document.createElement("div");
	var albumCover = document.createElement("img");
	
	albumInfo.className = "albumInfo";
	albumCover.className = "albumCover";
	
	// Set Cover image
	if (coverId != 0) {
		albumCover.src = "/PictureViewerWebServer/images/thumbnail.do"
				+ "?photoid=" + coverId;
	} else {
		albumCover.src = "resources/images/noimage.jpg";
	}

	albumInfo.appendChild(albumCover);
	albumInfo.appendChild(generateAlbumsRowName(id, name, subtitle));
	return albumInfo;
}

function generateAlbumsRowName(id, name, subtitle) {
	var albumNameDiv = document.createElement("div");
	var albumAnchor = document.createElement("a");
	var albumName = document.createElement("h3");
	var albumSubtitle = document.createElement("h4");

	albumNameDiv.className = "albumName";
	albumAnchor.href = "photos.do" + "?albumId=" + id;

	// Set Album Name
	albumName.innerText = name;
	albumSubtitle.innerText = subtitle;
	
	albumAnchor.appendChild(albumName);
	albumNameDiv.appendChild(albumAnchor);
	albumNameDiv.appendChild(albumSubtitle);
	return albumNameDiv;
}

function generateAlbumsRowTag(albumid) {
	var albumTagDiv = document.createElement("div");
	var albumTagTable = document.createElement("table");
	var albumAddCategory = document.createElement("button");
	
	getAdditionAlbumTagInfo(albumid, albumTagTable);
	
	// Set Category
	albumAddCategory.onclick = function() {
		showAddCategory(albumid, albumTagDiv, albumTagTable);
	}
	albumAddCategory.innerText = 'New Category';

	albumTagDiv.appendChild(albumTagTable);
	albumTagDiv.appendChild(albumAddCategory);
	return albumTagDiv;
}