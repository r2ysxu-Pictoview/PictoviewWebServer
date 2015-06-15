var searchCriteria = 1;

function addSearchCriteria() {
	$('#tagSearchTable').append(generateCriteria());
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
	console.log(data);
	
	$.post('search.do', data, function(response) {
		populateSearchResults(JSON.parse(response));
	}).fail(function() {
		alert("Response Failed");
	});
	searchCriteria = 1;
}

function populateSearchResults(json) {
	$('#albumsTable').empty();
	$.each(json, function(i, value) {
		var albumA = generateAlbumRow(value.id, value.coverId, value.name,
				value.subtitle)
		console.log(albumA);
		$("#albumsTable").append(albumA);
	});
}