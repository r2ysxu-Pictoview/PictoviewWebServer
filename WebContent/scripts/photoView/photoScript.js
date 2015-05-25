function getOriginalImage(index, id) {
	currentPhotoIndex = index;
	var originalImage = document.getElementById("enlargedPhoto");
	originalImage.src = "images.do" + "?photoid=" + id;
	var modalImageDiv = document.getElementById("imageModal");
	modalImageDiv.className = "modalDialog showDialog";
}

function getNextOriginalImage() {
	if (currentPhotoIndex < photoJson.length - 1) {
		var nextIndex = currentPhotoIndex + 1;
		getOriginalImage(nextIndex, photoJson[nextIndex].id);
	}
}

function getPrevOriginalImage() {
	if (currentPhotoIndex > 0) {
		var prevIndex = currentPhotoIndex - 1;
		getOriginalImage(prevIndex, photoJson[prevIndex].id);
	}
}

function closeModal() {
	var modalImageDiv = document.getElementById("imageModal");
	modalImageDiv.className = "modalDialog";
}