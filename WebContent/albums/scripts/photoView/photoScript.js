var currentPhotoIndex = -1;

function getOriginalImage(index, id) {
	currentPhotoIndex = index;
	var originalImage = document.getElementById("enlargedPhoto");
	originalImage.src = "images.do" + "?photoid=" + id;
	var modalImageDiv = document.getElementById("imageModal");
	modalImageDiv.className = "modalDialog showDialog";
}

function getNextOriginalImage() {
	var photoCount = $('#photoCount')[0].innerText;
	if (currentPhotoIndex < photoCount - 1) {
		var nextIndex = currentPhotoIndex + 1;
		var imgElem = $('#photos')[0].children[nextIndex];
		var id = imgElem.id.split("-")[1];
		getOriginalImage(nextIndex, id);
	}
}

function getPrevOriginalImage() {
	if (currentPhotoIndex > 0) {
		var prevIndex = currentPhotoIndex - 1;
		var id = $('#photos')[0].children[prevIndex].id.split("-")[1];
		getOriginalImage(prevIndex, id);
	}
}

function closeModal(modal) {
	var modalImageDiv = document.getElementById(modal);
	modalImageDiv.className = "modalDialog";
}

function showUploadForm() {
	var modalImageDiv = document.getElementById("uploadModal");
	modalImageDiv.className = "modalDialog showDialog";
}