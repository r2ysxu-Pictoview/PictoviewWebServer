<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Albums</title>
<link rel="stylesheet" type="text/css" href="resources/css/gallery.css" />
<link rel="stylesheet" type="text/css" href="resources/css/galleryModal.css" />
<script type="text/javascript" src="scripts/albumView/albumScript.js"></script>
<script type="text/javascript" src="scripts/albumView/tagScript.js"></script>
<script type="text/javascript" src="scripts/albumView/searchScript.js"></script>
<script type="text/javascript" src="scripts/jquery-2.1.3.min.js"></script>
</head>
<body>
	<div id="headerDiv">
		<div id="searchAlbumDiv">
			<label for="albumSearchInput">Name</label>
			<input type="text" id="albumSearchInput" />
			<table id="tagSearchTable">
				<thead>
				  <tr>
				     <th>Category</th>
				     <th>Tags</th>
				  </tr>
				 </thead>
				<tbody>
					<tr>
						<td>
							<input type="text" id="albumSearchCateInput0" value="tags" />
						</td><td>
							<input type="text" id="albumSearchTagInput0" />
						</td><td>
							<input type="button" id="addAlbumSearchTag" value="+" onclick="addSearchCriteria()" />
						</td>
					</tr>
				</tbody>
			</table>
			<button id="searchButton" onclick="submitSearchQuery()">Search</button>
		</div>
		<button onclick="showNewAlbumCommand(0)">New Album</button>
	</div>
	<table id="albumsTable" class="albumsTable">
		<c:forEach var="album" items="${albumList}">
			<tr>
				<td id="albumCell-${album.id}" class="albumCell">
					<div class="albumContent">
						<div class="albumInfo">
							<c:choose>
								<c:when test="${album.coverId == 0}">
									<img src="resources/images/noimage.jpg" class="albumCover" />
								</c:when>
								<c:otherwise>
									<img src="images/thumbnail.do?photoid=${album.coverId}"
										class="albumCover" />
								</c:otherwise>
							</c:choose>
							<div class="albumName">
								<a href="photos.do?albumId=${album.id}"><h3>${album.name}</h3></a>
								<h4>${album.subtitle}</h4>
							</div>
						</div>
						<input type="image" src="resources/images/expandButton.png" id="albumExpandButton-${album.id}" class="albumExpandButton" onclick="expandAlbum(this)" />
					</div>
					<div class="albumExpanded">
						<div id="albumTagInfo-${album.id}"></div>
						<div id="albumExpand-${album.id}"></div>
						<table id="albumTable-${album.id}" class="albumsTable"></table>
					</div>
				</td>
			</tr>
		</c:forEach>
	</table>
	<div id="albumModal" class="modalDialog">
		<div class="modalFrame">
			<div class="modalDiv">
				<h4>Create New Album</h4>
				<form id="createForm" method="POST" action="/PictureViewerWebServer/albums/create.do">
					<input type="hidden" id="newAlbumParentId" name="parentId" value="0" />
					<label for="newAlbumName">Name</label>
					<input type="text" id="newAlbumName" name="albumName" />
					<button id="createButton">New Album</button>
				</form>
			</div>
		</div>
		<div class="closeModal" onclick="closeModal()"></div>
	</div>
</body>
</html>