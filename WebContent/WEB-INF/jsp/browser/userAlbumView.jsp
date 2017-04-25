<html ng-app="albumViewApp">
<head>
	<title>Albums</title>
	<link rel="stylesheet" type="text/css" href="resources/css/gallery.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/header.css" />
	<script type="text/javascript" src="../lib/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="../lib/angular.min.js"></script>
	<script type="text/javascript" src="scripts/controller/albumController.js"></script>
</head>
<body ng-controller="AlbumViewController">

	<!-- header -->
	<div style="margin-top:50px"></div>

	<!-- Search Bar -->
	<div id="albumSearchMenu" class="searchMenu">
			<div class="searchBar">
				<div class="homeButton" >Home</div>
				<div class="searchBox">
					<input type="text" id="albumSearchInput" ng-model="search.name" ng-value="name" placeholder="Search" class="searchBarInput" />
					<input type="image" id="searchSubmit" value="Search" src="resources/images/search-icon-hi.png" ng-click="submitSearch()" />
					<input type="image" id="addNewSearchCategory" src="resources/images/menuBarIcon.png" value="Add Category Criteria" ng-click="showCriteria()" />
					<div id="searchAdditionalCriteria" style="display:none">
					<table id="searchTagTable" class="searchTagTable">
						<thead>
						  <tr>
						     <th width="30%">Category</th>
						     <th width="65%">Tags</th>
						     <th width="5%"></th>
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
								<td>
									<input type="button" ng-if="$index == 0" value="+" ng-click="addCriteria()" />
									<input type="button" ng-if="$index > 0" value="-" ng-click="minusCriteria($index)" />
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- Sidebar -->
	<div class="sidebar">
		<div class="side_navbar">
			<div>
				<span>Home</span>
			</div>
			<div>
				<span>Public Albums</span>
			</div>
			<div>
				<span>Subscriptions</span>
			</div>
			<div>
				<span>My Albums</span>
			</div>
		</div>
		<div class="side_optionbar">
			<div>
				<span>New Album</span>
			</div>
		</div>
	</div>

	<!-- Full View -->
	<div class="fullview_container">
		<div class="breadcrumb">
			<span ng-repeat="subAlbum in albumPath" class="breadcrumbs" ng-class="{'left_chevon':!$last}" ng-click="expandSubalbumsPath(subAlbum.id, $index)">
				{{subAlbum.name}}
			</span>
		</div>
		<div ng-repeat="albumGroup in albumChunks" ng-include="'resources/templates/albumFullRowTemplate.html'"></div>
	</div>
</body>
</html>
