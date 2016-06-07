<html ng-app="albumViewApp">
<header>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Albums</title>
	<link rel="stylesheet" type="text/css" href="resources/css/gallery.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/galleryModal.css" />
	<script type="text/javascript" src="../lib/angular.min.js"></script>
	<script type="text/javascript" src="scripts/controller/albumController.js"></script>
</header>
<body ng-controller="AlbumViewController">
	<!-- Banner -->
	<div id="banner" class="banner">
		<div class="bannerContent">
			<img id="userIcon" src="resources/images/noimage.jpg" class="userCover" />
			<div id="userInformation" class="leftControl">
				<h4>User Name</h4>
			</div>
			<div id="bannerControl" class="rightControl">
				<button>Home</button>
				<button>Logout</button>
			</div>
		</div>
	</div>

	<div class="centerBG"></div>
	<div class="centerDiv">
		<!-- Sidebar -->
		<div id="sidebar" class="sidebar">
			<!-- Search Bar -->
			<div id="albumSearchMenu" class="sideMenu">
				<div class="searchBar" >
					<h3>Search</h3>
					<div class="searchBox">
						<img src="resources/images/search-icon-hi.png" class="searchBarIcon" />
						<input type="text" id="albumSearchInput" ng-model="search.name" ng-value="name" class="searchBarInput" />
					</div>
					<table id="searchTagTable" class="searchTagTable">
						<thead>
						  <tr>
						     <th width="30%">Category</th>
						     <th width="70%">Tags</th>
						  </tr>
						 </thead>
						<tbody>
							<tr ng-repeat="cate in categories">
								<td>
									<input type="text" value="{{cate.category}}">
								</td>
								<td>
									<input type="text" value="{{cate.tags}}">
								</td>
							</tr>
						</tbody>
					</table>
					<input type="button" id="addNewSearchCategory" value="Add Category Criteria" ng-click="addCriteria()" class="criteriaButton" />
					<input type="button" id="searchSubmit" value="Search" class="criteriaButton" ng-click="submitSearch()" />
				</div>
			</div>
			<br />
			<div id="favouriteBar" class="sideMenu">
					<h3>Favourites</h3>
					No favourites
			</div>
			<br />
			<div id="albumOptions" class="sideMenu">
				<input type="button" id="createNewAlbumButton" value="New Album" ng-click="showCreateAlbumModal()" class="criteriaButton" />
			</div>
		</div>

		<!-- Content -->
		<div id="contentMenu" class="content">
			<div id="albumListMenu">
				<table class="albumsTable">
					<tr ng-repeat="album in albumList" ng-include="'resources/templates/albumTemplate.html'">
					</tr>
				</table>
			</div>
		</div>
	</div>
	
	<!-- Modals -->
	<div id="createAlbumModal" ng-class="modalDialog">
		<div class="modalFrame">
			<div class="modalDiv largeText">
				<h3>Create New Album</h3>
				<form id="createForm" method="POST" enctype="multipart/form-data" action="/PictureViewerWebServer/albums/create.do?${_csrf.parameterName}=${_csrf.token}">
					<input type="hidden" id="newAlbumParentId" name="parentId" value="0" />
		    		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		    		
					<label for="albumName">Name</label>
					<input type="text" id="albumName" name="albumName" />
					<br />
					<label for="newAlbumDescription">Description</label>
					<textarea rows="4" cols="50" id="newAlbumDescription" ></textarea>
					<br />
					<label for="uploadPhoto">Choose File</label>
					<input type="file" name="file">
					<br />
					<button type="submit" id="createButton">New Album</button>
				</form>
			</div>
		<div class="closeModal" ng-click="closeAlbumModal()"></div>
		</div>
	</div>
</body>
</html>