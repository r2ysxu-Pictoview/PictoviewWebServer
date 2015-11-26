<html ng-app="albumViewApp">
<header>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Albums</title>
	<link rel="stylesheet" type="text/css" href="resources/css/gallery.css" />
	<script type="text/javascript" src="../lib/angular.min.js"></script>
	<script type="text/javascript" src="scripts/controller/albumController.js"></script>

	<script type="text/ng-template"  id="albumTree.html">
		<td id="albumCell-{{album.id}}" class="albumCell">
			<div class="albumContent">
				<div class="albumInfo">
					<img ng-src="images/thumbnail.do?photoid={{album.coverId}}" class="albumCover" />
					<a href="photos.do?albumId={{album.id}}">
						<div class="albumName">
							<h3>{{album.name}}</h3>
							<h4>{{album.subtitle}}</h4>
						</div>
					</a>
				</div>
				<input type="image" src="resources/images/expandButton.png" id="albumExpandButton-{{album.id}}" class="albumExpandButton" ng-click="expandAlbum(album)" />
			</div>
			<div class="albumExpanded">
				<div id="albumTagInfo-{{album.id}}" style="display:block;">
					<table class="albumTagTable">
						<tr ng-repeat="cate in album.categories">
							<td width="30%">{{cate.category}}</td>
							<td width="70%">
								<div class="tagNameDiv" ng-repeat="tag in cate.tags">{{tag.tagName}}</div>
							</td>
						</tr>
					</table>
				</div>
				<div id="albumExpand-{{album.id}}"></div>
				<table id="subAlbums-{{album.id}}" class="albumsTable" style="display:block;">
					<tr ng-repeat="album in album.subalbums" ng-include="'albumTree.html'"></tr>
				</table>
			</div>
		</td>
	</script>
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
		</div>

		<!-- Content -->
		<div id="contentMenu" class="content">
			<div id="albumListMenu">
				<table class="albumsTable">
					<tr ng-repeat="album in albumList" ng-include="'albumTree.html'">
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>