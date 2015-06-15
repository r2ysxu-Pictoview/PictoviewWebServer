function expandAlbum(td) {
	event.stopPropagation();
	var albumId = td.id.split("-")[1];
	generateAlbumControlOptions(albumId);
	getAlbumsResponse(albumId);
	return true;
}

function generateAlbumControlOptions(albumId) {
	if ($("#albumExpand-" + albumId).is(':empty')) {
		var newAlbum = document.createElement('button');
		newAlbum.innerText = 'New Album';
		newAlbum.onclick = function () {
			showNewAlbumCommand(albumId);
		}
		$('#albumExpand-' + albumId).append(newAlbum);
	} else {
		$('#albumExpand-' + albumId).toggle();
	}
	
	if ($('#albumTagInfo-'+ albumId).is(':empty')) {
		var albumsRowTag = generateAlbumsRowTag(albumId);
		$('#albumTagInfo-'+ albumId).append(albumsRowTag);
	} else {
		$('#albumTagInfo-'+ albumId).toggle();
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

function generateAlbumRow(albumid, coverId, name, subtitle) {
	var albumRow = document.createElement("tr");
	var albumCell = document.createElement("td");
	var albumExpanded = generateAlbumExpandedInformation(albumid);
	var albumContent = generateAlbumsContent(albumid, name, subtitle, coverId);

	albumCell.className = "albumCell";
	albumCell.id = "albumCell-" + albumid;

	// Append elements
	albumCell.appendChild(albumContent);
	albumCell.appendChild(albumExpanded);
	albumRow.appendChild(albumCell);
	return albumRow;
}

function generateAlbumsContent(albumid, name, subtitle, coverId) {
	var albumContent = document.createElement("div");
	var albumExpandButton = document.createElement("input");
	
	albumContent.className = "albumContent";
	
	albumExpandButton.id = "albumExpandButton-" + albumid;
	albumExpandButton.type = "image";
	albumExpandButton.src= "resources/images/expandButton.png";
	albumExpandButton.className = "albumExpandButton";
	albumExpandButton.onclick = function() {
		return expandAlbum(albumExpandButton);
	};
	
	albumContent.appendChild(generateAlbumsContentInfo(albumid, name, subtitle, coverId));
	albumContent.appendChild(albumExpandButton);
	
	return albumContent;
}

function generateAlbumExpandedInformation(albumid) {
	var albumExpanded = document.createElement("div");
	var albumTags = document.createElement("div");
	var albumSubTable = document.createElement("table");
	var albumExpand = document.createElement("div");
	
	albumExpand.id = "albumExpand-" + albumid;
	
	albumTags.id = "albumTagInfo-" + albumid;

	albumSubTable.className = "albumsTable";
	albumSubTable.id = "albumTable-" + albumid;
	
	albumExpanded.appendChild(albumTags);
	albumExpanded.appendChild(albumExpand);
	albumExpanded.appendChild(albumSubTable);
	
	return albumExpanded;
}

function generateAlbumsContentInfo(id, name, subtitle, coverId) {
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
	albumInfo.appendChild(generateAlbumsNameDiv(id, name, subtitle));
	return albumInfo;
}

function generateAlbumsNameDiv(id, name, subtitle) {
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
		return showAddCategory(albumid, albumTagDiv, albumTagTable);
	}
	albumAddCategory.innerText = 'New Category';

	albumTagDiv.appendChild(albumTagTable);
	$('#albumExpand-'+albumid).append(albumAddCategory);
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