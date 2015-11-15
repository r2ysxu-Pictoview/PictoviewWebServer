<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Albums</title>
<link rel="stylesheet" type="text/css" href="resources/css/gallery.css" />
<link rel="stylesheet" type="text/css" href="resources/css/galleryModal.css" />
<link rel="stylesheet" type="text/css" href="resources/css/jquery-ui.min.css" />
<script type="text/javascript" src="../../lib/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="../../lib/jquery-ui.min.js"></script>
<script type="text/javascript" src="scripts/albumView/albumScript.js"></script>
<script type="text/javascript" src="scripts/albumView/tagScript.js"></script>
<script type="text/javascript" src="scripts/albumView/searchScript.js"></script>
</head>
<body>
	<div id="headerDiv" class="floatingHeader">
		<div class="bannerDiv">
			<div id="searchAlbumDiv">
			<form onsubmit="submitSearchQuery();return false;" >
				<div class="searchBar">
					<img src="resources/images/search-icon-hi.png" class="searchBarIcon" />
					<input type="text" id="albumSearchInput" class="searchBarInput" />
					<!-- iOS Hack -->
					<input type="submit" class="herebutnothere" />
				</div>
				<input type="image" id="showTagSearch" src="resources/images/menuBarIcon.png" onclick="toggleTagSearch(); return false;" class="searchShowTagTable" />
				<table id="searchTagTable" class="searchTagTable" style="display:none">
					<thead>
					  <tr>
					     <th width="35%">Category</th>
					     <th width="70%">Tags</th>
					     <th width="5%"></th>
					  </tr>
					 </thead>
					<tbody>
						<tr>
							<td>
								<input type="text" id="albumSearchCateInput0" value="tags" />
							</td><td>
								<input type="text" id="albumSearchTagInput0" />
							</td><td>
								<input type="button" id="addAlbumSearchTag" value="+" onclick="addSearchCriteria(); return false;" />
							</td>
						</tr>
					</tbody>
				</table>
				</form>
			</div>
		</div>
	</div>
	<div id="contentDiv" class="content">
		<input type="button" onclick="showNewAlbumCommand(0)" value="New Album" />
		<table id="albumsTable" class="albumsTable">
			<c:forEach var="album" items="${albumList}">
				<tr id="albumRow-${album.id}">
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
								<a href="photos.do?albumId=${album.id}">
									<div class="albumName">
										<h3>${album.name}</h3>
										<h4>${album.subtitle}</h4>
									</div>
								</a>
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
	</div>
	<div id="albumModal" class="modalDialog">
		<div class="modalFrame">
			<div class="modalDiv largeText">
				<h3>Create New Album</h3>
				<form id="createForm" method="POST" action="/PictureViewerWebServer/albums/create.do">
					<input type="hidden" id="newAlbumParentId" name="parentId" value="0" />
					<label for="newAlbumName">Name</label>
					<input type="text" id="newAlbumName" name="albumName" />
					<button id="createButton">New Album</button>
				</form>
			</div>
		</div>
		<div class="closeModal" onclick="closeAlbumModal()"></div>
	</div>
</body>
</html>