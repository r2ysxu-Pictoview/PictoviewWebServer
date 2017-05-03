<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Photos</title>
<link rel="stylesheet" type="text/css" href="resources/css/gallery.css" />
<link rel="stylesheet" type="text/css" href="resources/css/photo.css" />
<script type="text/javascript" src="../lib/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="../lib/hammer.min.js"></script>
<script type="text/javascript" src="scripts/controller/photoScript.js"></script>
</head>
<body>
	<p style="display:inline-block;color:white;">Displaying <span id="photoCount">${photoCount}</span> photos</p>
	<input type="button" value="Upload" onclick="showUploadForm()" class="showUploadButton" />
	<hr />
	<table id="albumTable-${albumId}" class="albumsTable"></table>
	<hr />
	<div id="photos" class="photoAlbum">
		<c:forEach var="photo" items="${photoList}" varStatus="status">
			<img id="photo-${photo.id}"
				src="images/thumbnail.do?photoid=${photo.id}" class="imageThumbnail"
				onclick="getOriginalImage(${status.index}, ${photo.id})" />
		</c:forEach>
	</div>
	<div id="uploadModal" class="modalDialog opaque">
		<div id="uploadDiv" class="modalFrame modalUplaod">
			<p>Please choose Images to Upload</p>
			<form method="POST" enctype="multipart/form-data" action="upload.do">
		    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<input id="albumIdInput" type="text" name="albumId" value="${albumId}" hidden />
				<label for="uploadPhoto">Choose File</label>
				<input type="file" name="file" multiple>
				<input type="submit" value="Upload"/>
			</form>
		</div>
		<div class="closeModal" onclick="closePhotoModal('uploadModal')"></div>
	</div>
	<div id="imageModal" class="modalDialog">
		<div id="nextButton" class="navbutton navbuttonPrev noselect" onclick="getPrevOriginalImage()">
			<div class="modalButtonIcon">
				<svg class="modalSvg modalSvgLeft"> <polyline
					stroke-linejoin="round" points="35,5 5,35 35,65" stroke="black"
					stroke-width="5" fill="none" /> </svg>
			</div>
		</div>
		<div id="prevButton" class="navbutton navbuttonNext noselect" onclick="getNextOriginalImage()">
			<div class="modalButtonIcon">
				<svg class="modalSvg modalSvgRight"> <polyline
					stroke-linejoin="round" points="5,5 35,35 5,65" stroke="black"
					stroke-width="5" fill="none" /> </svg>
			</div>
		</div>
		<img id="enlargedPhoto" class="fullImage noselect" />
		<div class="closeModal" onclick="closePhotoModal('imageModal')"></div>
	</div>
</body>
</html>