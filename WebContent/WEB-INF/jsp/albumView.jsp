<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Albums</title>
<link rel="stylesheet" type="text/css" href="resources/css/gallery.css" />
<script type="text/javascript" src="scripts/albumView/albumScript.js"></script>
<script type="text/javascript" src="scripts/albumView/tagScript.js"></script>
<script type="text/javascript" src="scripts/jquery-2.1.3.min.js"></script>
</head>
<body>
	<div id="headerDiv">
		<div id="createAlbumDiv">
			<div>
				<input type="text" id="albumSearchInput" />
				<button id="searchButton" onclick="searchAlbums(); return false;">Search</button>
			</div>
			<!-- <button id="createAlbum" onclick="createAlbum()">New Album</button> -->
		</div>
	</div>
	<table id="albumsTable" class="albumsTable">
		<c:forEach var="album" items="${albumList}">
			<tr>
				<td id="albumCell-${album.id}" class="albumCell" onclick="expandAlbum(this)">
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
							<a href="photos.do?albumId=${album.id}"><h4>${album.name}</h4></a>
							<h3>${album.subtitle}</h3>
						</div>
					</div>
					<table id="albumSub-${album.id}" class="albumsTable"></table>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>