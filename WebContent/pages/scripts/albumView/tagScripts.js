/* Album Info Related */
var allUserTags;

function getAdditionAlbumInfo(rowIndex) {
	var albumid = albumJson[rowIndex].id;
	if (albumid != null) {
		$.get('/PictureViewerWebServer/albums/tag/get.do', {
			"albumId" : albumid
		}, function(response) {
			createTagsDiv(JSON.parse(response), rowIndex);
		}).fail(function() {
			alert("Response Failed");
		});
	}
}

function createTagsDiv(tagResponseJson, rowIndex) {
    var albumsCell = document.getElementById("albumsTable").rows[rowIndex].children[0];
    
    invalidateExpandedRows();
    lastAccessedAlbum = rowIndex;
    
    var infoDiv = document.createElement("div");
    infoDiv.className = "albumInfoDiv";

	if (tagResponseJson.length == 0) {
	} else {
		var tagsTable = document.createElement("table");
		tagsTable.className = "albumTagTable";
		
	    $.each(tagResponseJson, function(i, value) {
	        var categoryRow = generateTagsTableRow(albumJson[rowIndex].id, value.category, value.tags);
	        tagsTable.appendChild(categoryRow);
		});
	
	    infoDiv.appendChild(tagsTable);
	}
	albumsCell.appendChild(infoDiv);
}

function invalidateExpandedRows() {
	if (lastAccessedAlbum >= 0) {
		var albumsCell = document.getElementById("albumsTable").rows[lastAccessedAlbum].children[0];
	    if (albumsCell.children.length > 2) {
	    	while (albumsCell.children[2]) {
	    		albumsCell.removeChild(albumsCell.children[2]);
	    	}
	    }
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
	
    $.each(tagJson, function(i, value) {
        tagCell.appendChild(generateTagNameDiv(value.tagName));
	});
    
	tagCell.appendChild(tagButton);
	
	//Appending
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
		"categoryName"  : category,
		"tagName" : tagName
	}, function(response) {
	}).fail(function() {
		alert("Response Failed");
	});
	
	//Add tag to element
	tagCell.appendChild(generateTagNameDiv(tagName));
	
	//Remove input
	tagCell.removeChild(tagButtonDiv);
}