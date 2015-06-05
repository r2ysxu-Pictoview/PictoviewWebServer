function expandAlbum(td) {
	event.stopPropagation();
	var albumId = td.id.split("-")[1];
	generateAlbumControlOptions(albumId);
	getAlbumsResponse(albumId);
	return true;
}

function generateAlbumControlOptions(albumId) {
	if ($("#albumExpand-" + albumId).is(':empty')) {
		var button = document.createElement('button');
		button.innerText = 'New Album';
		button.onclick = function () {
			showNewAlbumCommand(albumId);
		}
		
		$('#albumExpand-' + albumId).append(button);
	} else {
		$('#albumExpand-' +albumId).toggle();
	}
}

function showNewAlbumCommand(albumId) {
	$('#newAlbumParentId')[0].value = albumId;
	$('#albumModal').addClass("showDialog");
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
	if ($("#albumTable-" + albumId).is(':empty')) {
		$.each(json, function(i, value) {
			var albumA = generateAlbumRow(value.id, value.coverId, value.name,
					value.subtitle)
			$("#albumTable-" + albumId).append(albumA);
		});
	}else {
		$('#albumTable-' +albumId).toggle();
	}
}

function generateAlbumRow(id, coverId, name, subtitle) {
	var albumRow = document.createElement("tr");
	var albumCell = document.createElement("td");
	var albumSubTable = document.createElement("table");
	var albumExpand = document.createElement("div");

	// Setting class
	albumCell.className = "albumCell";
	albumSubTable.className = "albumsTable";
	
	albumCell.id = "albumCell-" + id;
	albumSubTable.id = "albumTable-" + id;
	albumExpand.id = "albumExpand-" + id;
	
	albumCell.onclick = function() {
		return expandAlbum(albumCell);
	};
	
	var albumsRowTag = generateAlbumsRowTag(id);
	$(albumsRowTag).toggle();

	// Append elements
	albumCell.appendChild(generateAlbumsRowInfo(id, name, subtitle, coverId));
	albumCell.appendChild(albumsRowTag);
	albumCell.appendChild(albumExpand);
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
		albumCover.src = "/PictureViewerWebServer/albums/images/thumbnail.do"
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
	
	albumTagDiv.id = "albumTagInfo-" + albumid;
	
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

function createAlbum() {
	var albumName = $('#newAlbumName')[0].value;
	
	$.post('create.do', {
		"parentId" : 0,
		"albumName" : albumName
	}, function(response) {
	}).fail(function() {
		alert("Response Failed");
	});
	return false;
}

function closeModal() {
	$("#albumModal").removeClass('showDialog');
}