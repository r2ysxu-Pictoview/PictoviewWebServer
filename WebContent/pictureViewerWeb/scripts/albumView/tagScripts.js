/* Album Info Related */

function getAdditionAlbumInfo(rowIndex) {
	var albumid = albumJson[rowIndex].id;
	if (albumid != null) {
		$.get('albumServlet/tags', {
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
    var tagButton = document.createElement("button");
    
    // New Tag Button
    tagButton.innerText = "+";
    tagButton.onclick = function() {
    	uploadTag(rowIndex);
	};
    
    console.log(tagResponseJson);

	if (tagResponseJson.length == 0) {
	} else {
		var tagsTable = document.createElement("table");
		
	    $.each(tagResponseJson, function(i, value) {
	        var categoryRow = generateTagsTableRow(value.category, value.tags);
	        tagsTable.appendChild(categoryRow);
		});
	
	    infoDiv.appendChild(tagsTable);
	}
	
	infoDiv.appendChild(tagButton);
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

function uploadTag(rowIndex) {
	var tagModalDiv = document.getElementById("tagModal");
	tagModalDiv.className = "modalDialog showDialog";
}

function generateTagsTableRow(category, tagJson) {
	var tagRow = document.createElement("tr");
	var cateCell = document.createElement("td");
	var tagCell = document.createElement("td");
	
	cateCell.innerText = category;
	
    $.each(tagJson, function(i, value) {
    	var tagDiv = document.createElement("div");
    	tagDiv.innerText = value.tagName;
        tagCell.appendChild(tagDiv);
	});
	
	//Appending
    tagRow.appendChild(cateCell);
	tagRow.appendChild(tagCell);
	return tagRow;
}

function closeTagModal() {
	var tagModalDiv = document.getElementById("tagModal");
	tagModalDiv.className = "modalDialog";
}

function tagAlbum() {
	if (lastAccessedAlbum != -1) {
		var albumid = albumJson[lastAccessedAlbum].id;
		var cateid = document.getElementById("tagCategory").value;
		var tagName = document.getElementById("tagInput").value;
		console.log(albumid);
		$.get('albumServlet/tags/create', {
			"albumId" : albumid,
			"cateId"  : cateid,
			"tagName" : tagName
		}, function(response) {
			createTagsDiv(JSON.parse(response), rowIndex);
		}).fail(function() {
			alert("Response Failed");
		});
	}
}