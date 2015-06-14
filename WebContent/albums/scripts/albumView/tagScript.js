/* Album Info Related */
var allUserTags;

function showAddCategory(albumid, albumTagDiv, table) {
	var createCategoryDiv = document.createElement('div');
	var categoryInput = document.createElement('input');
	var categoryAdd = document.createElement('button');

	categoryAdd.innerText = 'New Category';

	categoryAdd.onclick = function() {
		albumTagDiv.removeChild(createCategoryDiv);
		return addCategory(albumid, table, categoryInput);
	}

	createCategoryDiv.appendChild(categoryInput);
	createCategoryDiv.appendChild(categoryAdd);
	albumTagDiv.appendChild(createCategoryDiv);
	return true;
}

function addCategory(albumid, table, categoryInput) {
	var tr = document.createElement('tr');
	var categoryCell = document.createElement('td');
	var category = categoryInput.value;

	categoryCell.innerText = category;

	tr.appendChild(categoryCell);
	tr.appendChild(createEmptyTagsCell(albumid, category));
	table.appendChild(tr);
	return true;
}

function createEmptyTagsCell(albumid, category) {
	var tagsCell = document.createElement('td');
	var tagCreateDiv = generateAddCellElements(tagsCell, albumid, category);

	tagsCell.appendChild(tagCreateDiv);
	return tagsCell;
}

function getAdditionAlbumTagInfo(albumid, albumTagTable) {
	if (albumid != null) {
		$.get('/PictureViewerWebServer/albums/tag/get.do', {
			"albumId" : albumid
		}, function(response) {
			createTagsDiv(JSON.parse(response), albumid, albumTagTable);
		}).fail(function() {
			alert("Response Failed");
		});
	}
}

function createTagsDiv(tagResponseJson, albumid, tagsTable) {
	if (tagResponseJson.length == 0) {
	} else {
		tagsTable.className = "albumTagTable";

		$.each(tagResponseJson, function(i, value) {
			var categoryRow = generateTagsTableRow(albumid,
					value.category, value.tags);
			tagsTable.appendChild(categoryRow);
		});
	}
}

function generateTagsTableRow(albumid, category, tagJson) {
	var tagRow = document.createElement("tr");
	var cateCell = document.createElement("td");
	var tagCell = document.createElement("td");

	var tagButton = document.createElement("button");

	// New Tag Button
	tagButton.innerText = "+";
	tagButton.className = "tagAddButton";
	tagButton.onclick = function() {
		addNewTag(tagButton, tagCell, albumid, category);
	};

	cateCell.innerText = category;
	cateCell.width = "30%";

	tagCell.width = "70%";
	$.each(tagJson, function(i, value) {
		tagCell.appendChild(generateTagNameDiv(value.tagName));
	});

	tagCell.appendChild(tagButton);

	// Appending
	tagRow.appendChild(cateCell);
	tagRow.appendChild(tagCell);
	return tagRow;
}

function addNewTag(tagButton, tagCell, albumid, category) {
	tagCell.appendChild(generateAddCellElements(tagCell, albumid, category));

}

function generateAddCellElements(tagCell, albumid, category) {
	var tagButtonDiv = document.createElement("div");
	tagButtonDiv.id = "addTagButtonDiv";
	tagButtonDiv.className = "tagAddButtonDiv";
	var tagNameInput = document.createElement("input");
	tagNameInput.type = "text";
	var tagSubmit = document.createElement("button");
	tagSubmit.innerText = "+";

	tagSubmit.onclick = function() {
		tagAlbum(tagCell, tagButtonDiv, albumid, category, tagNameInput);
	};

	tagButtonDiv.appendChild(tagNameInput);
	tagButtonDiv.appendChild(tagSubmit);

	return tagButtonDiv;
}

function generateTagNameDiv(tagName) {
	var tagDiv = document.createElement("div");
	tagDiv.innerText = tagName;
	tagDiv.className = "tagNameDiv";
	return tagDiv;
}

function tagAlbum(tagCell, tagButtonDiv, albumid, category, tagNameInput) {
	var tagName = tagNameInput.value;
	console.log(albumid);
	$.get('/PictureViewerWebServer/albums/tag/create.do', {
		"albumId" : albumid,
		"categoryName" : category,
		"tagName" : tagName
	}, function(response) {
	}).fail(function() {
		alert("Response Failed");
	});

	// Add tag to element
	tagCell.appendChild(generateTagNameDiv(tagName));

	// Remove input
	tagCell.removeChild(tagButtonDiv);
}