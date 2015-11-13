var searchCriteria = 1;
var availableTags = [];

function addSearchCriteria() {
	$('#searchTagTable').append(generateCriteria());
}

function generateCriteria() {
	var tr = document.createElement('tr');
	var td1 = document.createElement('td');
	var td2 = document.createElement('td');
	var cateInput = document.createElement('input');
	var tagInput = document.createElement('input');
	
	cateInput.setAttribute('type', 'text');
	cateInput.id = "albumSearchCateInput" + searchCriteria;
	tagInput.setAttribute('type', 'text');
	tagInput.id = "albumSearchTagInput" + searchCriteria;
	
	td1.appendChild(cateInput);
	td2.appendChild(tagInput);
	tr.appendChild(td1);
	tr.appendChild(td2);
	
    $(tagInput).autocomplete({
      source: availableTags
    });
	
	searchCriteria += 1;
	return tr;
}

function matchSpaceQuotes(str) {
	return str.match(/(".*?"|[^" \s]+)(?=\s* |\s*$)/g) || [];
}

function submitSearchQuery() {
	var name =  matchSpaceQuotes($('#albumSearchInput').val());
	var data = {"name" : name, "cateNum": searchCriteria, "cate" : [] };
	for (var i = 0; i < searchCriteria; i++) {
		var category = $('#albumSearchCateInput' + i).val();
		var tags = matchSpaceQuotes($('#albumSearchTagInput' + i).val());
		data.cate.push({ "category" : category, "tags" : tags });
	}
	
	$.post('search.do', data, function(response) {
		populateSearchResults(JSON.parse(response));
	}).fail(function() {
		alert("Response Failed");
	});
	searchCriteria = 1;
	return false;
}

function populateSearchResults(json) {
	$('#albumsTable').empty();
	$.each(json, function(i, value) {
		var albumA = generateAlbumRow(value.id, value.coverId, value.name,
				value.subtitle);
		$("#albumsTable").append(albumA);
	});
}

function toggleTagSearch() {
	
	$.get('/PictureViewerWebServer/albums/category/get.do', function(response) {
		availableTags = JSON.parse(response);
	});
	
	$('#albumSearchCateInput0').autocomplete({
        source: availableTags
    });
	$('#searchTagTable').toggle();
}