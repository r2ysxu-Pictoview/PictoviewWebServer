function searchAlbums() {
	var nameQuery = $('#albumSearchInput')[0].value;
	var tagQuery = $('#albumSearchTagInput')[0].value;

	$.get('albums/search.do', {
		"nameQuery" : nameQuery,
		"tagQuery" : tagQuery
	}, function(response) {
		popluateSearchedAlbums(JSON.parse(response));
	}).fail(function() {
		alert("Response Failed");
	});
}

function popluateSearchedAlbums(json) {
	$('#albumsTable').empty();
	$.each(json, function(i, value) {
		var albumA = generateAlbumRow(value.id, value.coverId, value.name,
				value.subtitle)
		$('#albumsTable').append(albumA);
	});
}