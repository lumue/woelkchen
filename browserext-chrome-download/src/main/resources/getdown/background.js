chrome.contextMenus.onClicked.addListener(function(info, tab) {
	const contentUrl = [info.linkUrl];
	chrome.storage.sync.get({serviceUrl:'http://localhost:8001/'
	}, function(items) {
		const serviceUrl= items.serviceUrl
		fetch(serviceUrl+"/downloads", {
			method: "POST",
			headers: {
				'Accept': 'application/json, text/plain, */*',
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(contentUrl)
		})
	});

});

chrome.runtime.onInstalled.addListener(function() {
	chrome.contextMenus.create({
		id: 'open',
		title: chrome.i18n.getMessage('openContextMenuTitle'),
		contexts: ['link'],
	});
})